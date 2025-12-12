package com.katelee.monsterpedia.feature.encyclopedia.viewmodel

import app.cash.turbine.test
import com.katelee.monsterpedia.domain.model.MonsterDetail
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailIntent
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("MonsterDetailViewModel Tests")
class MonsterDetailViewModelTest {

    private lateinit var viewModel: MonsterDetailViewModel
    private lateinit var repo: MonsterRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk()
        viewModel = MonsterDetailViewModel(repo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("初始狀態應為空且未載入")
    fun `initial state should be empty and not loading`() {
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertNull(state.item)
        assertNull(state.error)
    }

    // JUnit 6 新特性：可以直接用 suspend！
    @Test
    @DisplayName("載入成功時應更新 item 並清除 loading 狀態")
    suspend fun `load intent should update state with item on success`() {
        // Given
        val monsterId = "monster-123"
        val monsterDetail = MonsterDetail(
            id = monsterId,
            name = "Test Monster",
            imageUrl = "https://example.com/fake-monster.jpg",
        )
        coEvery { repo.getDetail(monsterId) } returns monsterDetail

        // When & Then
        viewModel.state.test {
            // 初始狀態
            assertEquals(MonsterDetailState(), awaitItem())

            viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
            testDispatcher.scheduler.advanceUntilIdle()

            // Loading 狀態
            awaitItem().let { state ->
                assertTrue(state.isLoading)
                assertNull(state.item)
                assertNull(state.error)
            }

            // 成功狀態
            awaitItem().let { state ->
                assertFalse(state.isLoading)
                assertEquals(monsterDetail, state.item)
                assertNull(state.error)
            }
        }

        coVerify { repo.getDetail(monsterId) }
    }

    @Test
    @DisplayName("載入失敗時應更新 error 並清除 loading 狀態")
    suspend fun `load intent should update state with error on failure`() {
        // Given
        val monsterId = "monster-456"
        val exception = RuntimeException("Network error")
        coEvery { repo.getDetail(monsterId) } throws exception

        // When & Then
        viewModel.state.test {
            assertEquals(MonsterDetailState(), awaitItem())

            viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
            testDispatcher.scheduler.advanceUntilIdle()

            // Loading 狀態
            awaitItem().let { state ->
                assertTrue(state.isLoading)
                assertNull(state.error)
            }

            // 錯誤狀態
            awaitItem().let { state ->
                assertFalse(state.isLoading)
                assertNull(state.item)
                assertEquals(exception, state.error)
            }
        }

        coVerify { repo.getDetail(monsterId) }
    }

    @Test
    @DisplayName("Retry 應使用上次的 ID 重新載入")
    fun `retry intent should reload with last id`() {
        // Given
        val monsterId = "monster-789"
        val monsterDetail = MonsterDetail(id = monsterId, name = "Retry Monster", imageUrl = "https://example.com/fake-monster.jpg")
        coEvery { repo.getDetail(monsterId) } returns monsterDetail

        // When
        viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(MonsterDetailIntent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 2) { repo.getDetail(monsterId) }
    }

    @Test
    @DisplayName("Retry 在沒有 currentId 時不應執行")
    fun `retry intent should do nothing when no previous id`() {
        // When
        viewModel.onIntent(MonsterDetailIntent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { repo.getDetail(any()) }
    }

    @Test
    @DisplayName("連續載入不同 ID 應更新 currentId")
    fun `loading different ids should update current id`() {
        // Given
        val id1 = "monster-1"
        val id2 = "monster-2"
        val detail1 = MonsterDetail(id = id1, name = "Monster 1", imageUrl = "https://example.com/fake-monster1.jpg")
        val detail2 = MonsterDetail(id = id2, name = "Monster 2", imageUrl = "https://example.com/fake-monster2.jpg")

        coEvery { repo.getDetail(id1) } returns detail1
        coEvery { repo.getDetail(id2) } returns detail2

        // When
        viewModel.onIntent(MonsterDetailIntent.Load(id1))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(MonsterDetailIntent.Load(id2))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(MonsterDetailIntent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Retry 應使用最後的 id2
        coVerify(exactly = 2) { repo.getDetail(id2) }
        coVerify(exactly = 1) { repo.getDetail(id1) }
    }
}
package com.katelee.monsterpedia.feature.encyclopedia.viewmodel

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.katelee.monsterpedia.domain.model.MonsterDetail
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailEffect
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailIntent
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

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

    // ==================== 測試資料工廠 ====================
    private fun createTestMonster(
        id: String = "test-123",
        name: String = "Test Monster",
        imageUrl: String = "https://example.com/monster.jpg"
    ) = MonsterDetail(
        id = id,
        name = name,
        imageUrl = imageUrl
    )

    // ==================== 初始狀態測試 ====================

    @Test
    @DisplayName("初始狀態應為空且未載入")
    fun `initial state should be empty and not loading`() {
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertNull(state.item)
        assertNull(state.error)
    }

    // ==================== Load Intent 測試 ====================

    @Nested
    @DisplayName("Load Intent Tests")
    inner class LoadIntentTests {

        @Test
        @DisplayName("載入成功時應更新狀態並發送 Effect")
        suspend fun `should update state and emit effect on successful load`() {
            // Given
            val monsterId = "monster-123"
            val monster = createTestMonster(id = monsterId, name = "Dragon")
            coEvery { repo.getDetail(monsterId) } returns monster

            // When & Then - 測試 State
            viewModel.state.test {
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
                    assertEquals(monster, state.item)
                    assertNull(state.error)
                }
            }

            coVerify { repo.getDetail(monsterId) }
        }

        @Test
        @DisplayName("載入成功時應發送 NotifyTopBarIdUpdate Effect")
        suspend fun `should emit NotifyTopBarIdUpdate effect on load`() {
            // Given
            val monsterId = "monster-456"
            val monster = createTestMonster(id = monsterId)
            coEvery { repo.getDetail(monsterId) } returns monster

            // When & Then - 測試 Effect
            viewModel.effect.test {
                viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
                testDispatcher.scheduler.advanceUntilIdle()

                // 應該收到 TopBar ID 更新 Effect
                val effect = awaitItem()
                assertTrue(effect is MonsterDetailEffect.NotifyTopBarIdUpdate)
                assertEquals(monsterId, (effect as MonsterDetailEffect.NotifyTopBarIdUpdate).id)
            }
        }

        @Test
        @DisplayName("載入失敗時應更新錯誤狀態")
        suspend fun `should update error state on load failure`() {
            // Given
            val monsterId = "monster-789"
            val exception = RuntimeException("Network error")
            coEvery { repo.getDetail(monsterId) } throws exception

            // When & Then
            viewModel.state.test {
                skipItems(1) // 跳過初始狀態

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
        }

        @Test
        @DisplayName("Effect 應在載入開始時立即發送，不等待結果")
        suspend fun `should emit effect immediately without waiting for load result`() {
            // Given
            val monsterId = "test-monster"
            // 模擬一個「慢」的載入
            coEvery { repo.getDetail(monsterId) } coAnswers {
                delay(1000) // 假設需要 1 秒
                MonsterDetail(monsterId, "Test", "url")
            }

            // When & Then
            viewModel.effect.test {
                viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
                // 不需要 advanceUntilIdle()，因為 Effect 應該立即發送

                // Effect 應該馬上就收到，不用等載入完成
                val effect = awaitItem()
                assertTrue(effect is MonsterDetailEffect.NotifyTopBarIdUpdate)
                assertEquals(monsterId, (effect as MonsterDetailEffect.NotifyTopBarIdUpdate).id)

                // 此時載入可能還沒完成
            }
        }

        @Test
        @DisplayName("載入失敗也不影響 Effect 發送")
        suspend fun `load failure should not affect effect emission`() {
            // Given
            val monsterId = "fail-monster"
            coEvery { repo.getDetail(monsterId) } throws RuntimeException("Error")

            // When & Then
            viewModel.effect.test {
                viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
                testDispatcher.scheduler.advanceUntilIdle()

                // Effect 仍然應該發送（因為是獨立的協程）
                val effect = awaitItem()
                assertTrue(effect is MonsterDetailEffect.NotifyTopBarIdUpdate)
                assertEquals(monsterId, (effect as MonsterDetailEffect.NotifyTopBarIdUpdate).id)
            }
        }
    }

    // ==================== Retry Intent 測試 ====================

    @Nested
    @DisplayName("Retry Intent Tests")
    inner class RetryIntentTests {

        @Test
        @DisplayName("Retry 應使用上次的 ID 重新載入")
        fun `retry should reload with last id`() {
            // Given
            val monsterId = "monster-retry"
            val monster = createTestMonster(id = monsterId, name = "Retry Monster")
            coEvery { repo.getDetail(monsterId) } returns monster

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
        fun `retry should do nothing when no previous id`() {
            // When
            viewModel.onIntent(MonsterDetailIntent.Retry)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 0) { repo.getDetail(any()) }
        }

        @Test
        @DisplayName("失敗後 Retry 應能成功載入")
        suspend fun `retry after failure should succeed`() {
            // Given
            val monsterId = "monster-fail-then-success"
            val monster = createTestMonster(id = monsterId)

            // 第一次失敗
            coEvery { repo.getDetail(monsterId) } throws RuntimeException("Timeout")
            viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
            testDispatcher.scheduler.advanceUntilIdle()

            // Retry 成功
            coEvery { repo.getDetail(monsterId) } returns monster

            viewModel.state.test {
                skipItems(1) // 跳過當前錯誤狀態

                viewModel.onIntent(MonsterDetailIntent.Retry)
                testDispatcher.scheduler.advanceUntilIdle()

                // Loading
                assertTrue(awaitItem().isLoading)

                // Success
                awaitItem().let { state ->
                    assertFalse(state.isLoading)
                    assertEquals(monster, state.item)
                    assertNull(state.error)
                }
            }
        }
    }

    // ==================== ColorExtracted Intent 測試 ====================

    @Nested
    @DisplayName("ColorExtracted Intent Tests")
    inner class ColorExtractedTests {

        @Test
        @DisplayName("ColorExtracted 應發送 NotifyTopBarColorUpdate Effect")
        suspend fun `ColorExtracted should emit NotifyTopBarColorUpdate effect`() {
            // Given
            val extractedColor = Color.Red

            // When & Then
            viewModel.effect.test {
                viewModel.onIntent(MonsterDetailIntent.ColorExtracted(extractedColor))
                testDispatcher.scheduler.advanceUntilIdle()

                // 應該收到顏色更新 Effect
                val effect = awaitItem()
                assertTrue(effect is MonsterDetailEffect.NotifyTopBarColorUpdate)
                assertEquals(extractedColor, (effect as MonsterDetailEffect.NotifyTopBarColorUpdate).color)
            }
        }

        @Test
        @DisplayName("多次 ColorExtracted 應發送多個 Effect")
        suspend fun `multiple ColorExtracted should emit multiple effects`() {
            // Given
            val colors = listOf(Color.Red, Color.Blue, Color.Green)

            // When & Then
            viewModel.effect.test {
                colors.forEach { color ->
                    viewModel.onIntent(MonsterDetailIntent.ColorExtracted(color))
                    testDispatcher.scheduler.advanceUntilIdle()

                    val effect = awaitItem()
                    assertTrue(effect is MonsterDetailEffect.NotifyTopBarColorUpdate)
                    assertEquals(color, (effect as MonsterDetailEffect.NotifyTopBarColorUpdate).color)
                }
            }
        }

        @Test
        @DisplayName("ColorExtracted 不應影響 State")
        fun `ColorExtracted should not affect state`() {
            // Given
            val color = Color.Yellow

            // When
            val stateBefore = viewModel.state.value
            viewModel.onIntent(MonsterDetailIntent.ColorExtracted(color))
            testDispatcher.scheduler.advanceUntilIdle()
            val stateAfter = viewModel.state.value

            // Then - State 應該不變
            assertEquals(stateBefore, stateAfter)
        }
    }

    // ==================== Effect 順序測試 ====================

    @Nested
    @DisplayName("Effect Order Tests")
    inner class EffectOrderTests {

        @Test
        @DisplayName("Load 後接 ColorExtracted 應依序發送兩個 Effect")
        suspend fun `Load followed by ColorExtracted should emit effects in order`() {
            // Given
            val monsterId = "monster-color"
            val monster = createTestMonster(id = monsterId)
            val color = Color.Cyan
            coEvery { repo.getDetail(monsterId) } returns monster

            // When & Then
            viewModel.effect.test {
                // Load
                viewModel.onIntent(MonsterDetailIntent.Load(monsterId))
                testDispatcher.scheduler.advanceUntilIdle()

                val effect1 = awaitItem()
                assertTrue(effect1 is MonsterDetailEffect.NotifyTopBarIdUpdate)
                assertEquals(monsterId, (effect1 as MonsterDetailEffect.NotifyTopBarIdUpdate).id)

                // ColorExtracted
                viewModel.onIntent(MonsterDetailIntent.ColorExtracted(color))
                testDispatcher.scheduler.advanceUntilIdle()

                val effect2 = awaitItem()
                assertTrue(effect2 is MonsterDetailEffect.NotifyTopBarColorUpdate)
                assertEquals(color, (effect2 as MonsterDetailEffect.NotifyTopBarColorUpdate).color)
            }
        }
    }

    // ==================== 邊界情況測試 ====================

    @Nested
    @DisplayName("Edge Cases")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("連續載入不同 ID 應更新 currentId")
        fun `loading different ids should update current id`() {
            // Given
            val id1 = "monster-1"
            val id2 = "monster-2"
            val detail1 = createTestMonster(id = id1, name = "Monster 1")
            val detail2 = createTestMonster(id = id2, name = "Monster 2")

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

        @Test
        @DisplayName("快速連續多次 Load 應都執行")
        suspend fun `rapid consecutive loads should all execute`() {
            // Given
            val ids = listOf("id-1", "id-2", "id-3")
            ids.forEach { id ->
                coEvery { repo.getDetail(id) } returns createTestMonster(id = id)
            }

            // When & Then - 測試 Effect
            viewModel.effect.test {
                ids.forEach { id ->
                    viewModel.onIntent(MonsterDetailIntent.Load(id))
                    testDispatcher.scheduler.advanceUntilIdle()

                    val effect = awaitItem()
                    assertEquals(id, (effect as MonsterDetailEffect.NotifyTopBarIdUpdate).id)
                }
            }

            // 每個 ID 都應該被呼叫一次
            ids.forEach { id ->
                coVerify(exactly = 1) { repo.getDetail(id) }
            }
        }
    }
}
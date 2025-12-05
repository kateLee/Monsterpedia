package com.katelee.monsterpedia.di

import com.katelee.monsterpedia.data.repository.DigimonRepositoryImpl
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DigimonModule {

    @Binds
    @Singleton
    abstract fun bindMonsterRepository(
        impl: DigimonRepositoryImpl
    ): MonsterRepository
}
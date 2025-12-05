package com.katelee.monsterpedia.di

import com.katelee.monsterpedia.data.repository.PokemonRepositoryImpl
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PokemonModule {

    @Binds
    @Singleton
    abstract fun bindMonsterRepository(
        impl: PokemonRepositoryImpl
    ): MonsterRepository
}
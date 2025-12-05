package com.katelee.monsterpedia.di

import com.katelee.monsterpedia.data.remote.PokemonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokemonNetworkModule {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = "https://pokeapi.co/api/v2/"

    @Provides
    @Singleton
    fun providePokemonApi(retrofit: Retrofit): PokemonApi =
        retrofit.create(PokemonApi::class.java)
}
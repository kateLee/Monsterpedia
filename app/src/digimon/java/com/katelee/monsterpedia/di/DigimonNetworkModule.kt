package com.katelee.monsterpedia.di

import com.katelee.monsterpedia.data.remote.DigimonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DigimonNetworkModule {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = "https://digi-api.com/api/v1/"

    @Provides
    @Singleton
    fun provideDigimonApi(retrofit: Retrofit): DigimonApi =
        retrofit.create(DigimonApi::class.java)
}
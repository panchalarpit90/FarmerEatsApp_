package com.example.farmereats.di

import com.example.farmereats.network.ApiService
import com.example.farmereats.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesBaseUrl(): String = "https://sowlab.com/assignment/"

    @Provides
    @Singleton
    fun providesRetrofitBuilder(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun providesPostRepository(apiService: ApiService): PostRepository {
        return PostRepository(apiService)

    }

}





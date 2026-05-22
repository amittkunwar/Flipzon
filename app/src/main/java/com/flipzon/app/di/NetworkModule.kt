package com.flipzon.app.di

import com.flipzon.app.network.FlipzonApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://dummyjson.com/"
    @Provides @Singleton fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
    @Provides @Singleton fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    @Provides @Singleton fun provideApi(retrofit: Retrofit): FlipzonApi = retrofit.create(FlipzonApi::class.java)
}

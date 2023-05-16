package com.example.homework_2.di.module

import com.example.homework_2.data.networking.OauthPreference
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.data.networking.ZulipInterseptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class NetworkingModule {

    @Provides
    fun provideUrl(): String = "https://tinkoff-android-spring-2023.zulipchat.com/"

    @Provides
    fun provideOkhhtpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ZulipInterseptor(OauthPreference.AUTH_EMAIL, OauthPreference.AUTH_KEY))
        .build()

    @Provides
    internal fun provideZulipApi(retrofit: Retrofit): ZulipApi {
        return retrofit.create(ZulipApi::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(provideUrl())
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideConverterFactory(json: Json, contentType: MediaType): Converter.Factory {
        return json.asConverterFactory(contentType)
    }

    @Provides
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Singleton
    @Provides
    fun provideContentType(): MediaType {
        return "application/json".toMediaType()
    }
}
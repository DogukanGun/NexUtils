package com.dag.nexutils.network.jupiter

import com.dag.nexutils.network.jupiter.service.JupiterService
import com.dag.nexutils.network.jupiter.service.TradeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ObjectModule {
    @Provides
    @Singleton
    @Named("JupiterStake")
    fun provideJupiterRetrofit(
        httpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder().baseUrl("https://worker.jup.ag/")
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()
    }

    @Provides
    @Singleton
    fun provideJupiterService(
        @Named("JupiterStake") retrofit: Retrofit
    ):JupiterService {
        return retrofit.create(JupiterService::class.java)
    }

    @Provides
    @Singleton
    @Named("Trade")
    fun provideRetrofit(
        httpClient: OkHttpClient,
    ):Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://quote-api.jup.ag/") // Replace with the correct base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun provideTradeService(
        @Named("Trade") retrofit: Retrofit
    ): TradeService {
        return retrofit.create(TradeService::class.java)
    }


}
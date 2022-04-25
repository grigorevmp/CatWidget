package com.grigorevmp.catwidget.data.network

import android.content.Context
import com.grigorevmp.catwidget.data.dto.CatImageDto
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CatImageService {

    fun getPicture(context: Context): Call<CatImageDto> {
        val okHttpClient = OkHttpClient().newBuilder()
        val cache = Cache(context.cacheDir, 4000)

        okHttpClient.cache(cache).build()

        val loader = Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .client(okHttpClient.cache(cache).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = loader.create(CatImageApi::class.java)

        return service.getCatPicture()
    }
}
package com.grigorevmp.catwidget.data.network

import android.content.Context
import com.grigorevmp.catwidget.data.dto.DogImageDto
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DogImageService {

    fun getPicture(context: Context): Call<DogImageDto> {
        val okHttpClient = OkHttpClient().newBuilder()
        val cache = Cache(context.cacheDir, 4000)


        val loader = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breeds/image/")
            .client(okHttpClient.cache(cache).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = loader.create(DogImageApi::class.java)

        return service.getDogPicture()
    }
}
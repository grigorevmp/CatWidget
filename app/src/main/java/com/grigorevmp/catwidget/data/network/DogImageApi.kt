package com.grigorevmp.catwidget.data.network

import com.grigorevmp.catwidget.data.dto.CatImageDto
import com.grigorevmp.catwidget.data.dto.DogImageDto
import retrofit2.Call
import retrofit2.http.GET

interface DogImageApi {
    @GET("random")
    fun getDogPicture() : Call<DogImageDto>
}
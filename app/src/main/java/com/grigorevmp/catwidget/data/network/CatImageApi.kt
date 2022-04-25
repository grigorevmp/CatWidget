package com.grigorevmp.catwidget.data.network

import com.grigorevmp.catwidget.data.dto.CatImageDto
import retrofit2.Call
import retrofit2.http.GET

interface CatImageApi {
    @GET("meow")
    fun getCatPicture() : Call<CatImageDto>
}
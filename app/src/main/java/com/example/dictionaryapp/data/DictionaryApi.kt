package com.example.dictionaryapp.data

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.dictionaryapp.data.Models

interface DictionaryApi {
    @GET("entries/en/{word}")
    suspend fun getWordMeaning(@Path("word") word: String): List<Models.WordResponse>
}

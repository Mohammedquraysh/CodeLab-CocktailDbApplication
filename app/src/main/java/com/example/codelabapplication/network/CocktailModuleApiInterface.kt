package com.example.codelabapplication.network

import com.example.codelabapplication.model.CocktailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailModuleApiInterface {

    @GET("search.php")
    suspend fun getCocktailDetails(@Query("f") cocktail: String): Response<CocktailResponse>
}
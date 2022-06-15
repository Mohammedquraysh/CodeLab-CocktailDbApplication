package com.example.codelabapplication.repository

import com.example.codelabapplication.model.CocktailResponse
import retrofit2.Response

interface CocktailRepositoryInterface {
    suspend fun getCocktailDetails (cocktail: String) : Response<CocktailResponse>
}
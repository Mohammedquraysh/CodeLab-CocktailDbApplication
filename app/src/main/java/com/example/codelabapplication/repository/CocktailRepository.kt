package com.example.codelabapplication.repository

import com.example.codelabapplication.model.CocktailResponse
import com.example.codelabapplication.network.CocktailModuleApiInterface
import retrofit2.Response
import javax.inject.Inject

class CocktailRepository @Inject constructor(private val cocktailModuleApiInterface: CocktailModuleApiInterface): CocktailRepositoryInterface {
    override suspend fun getCocktailDetails(cocktail: String): Response<CocktailResponse> {
        return cocktailModuleApiInterface.getCocktailDetails(cocktail)
    }
}
package com.example.codelabapplication.dependencyinjection

import com.example.codelabapplication.network.CocktailModuleApiInterface
import com.example.codelabapplication.repository.CocktailRepository
import com.example.codelabapplication.repository.CocktailRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCocktailRepository(cocktailModuleApiInterface: CocktailModuleApiInterface):CocktailRepositoryInterface{
        return CocktailRepository(cocktailModuleApiInterface)
    }

}
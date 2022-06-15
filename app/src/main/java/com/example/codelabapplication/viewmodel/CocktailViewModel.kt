package com.example.codelabapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codelabapplication.model.CocktailResponse
import com.example.codelabapplication.repository.CocktailRepository
import com.example.codelabapplication.repository.CocktailRepositoryInterface
import com.example.codelabapplication.util.ApiCallNetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CocktailViewModel @Inject constructor(private val cocktailRepository: CocktailRepositoryInterface): ViewModel(){

    /** cocktail db LiveData **/
    private val _cocktailResponseLiveData: MutableLiveData<ApiCallNetworkResource<CocktailResponse>> = MutableLiveData()
    val cocktailResponseLiveData: LiveData<ApiCallNetworkResource<CocktailResponse>> = _cocktailResponseLiveData


    /**Handling Network Error for cocktail details ans save to livedata*/
    fun getCocktailDetails(cocktail: String) {
        viewModelScope.launch {
            _cocktailResponseLiveData.postValue(ApiCallNetworkResource.Loading())
            try {
                delay(1)
                val response = cocktailRepository.getCocktailDetails(cocktail)
                if (response.isSuccessful) {
                    _cocktailResponseLiveData.postValue(
                        ApiCallNetworkResource.Success(
                            response.body()!!.toString(), response.body()
                        )
                    )
                } else {

                    _cocktailResponseLiveData.postValue(ApiCallNetworkResource.Error(response!!.message()))
                }

            } catch (e: Throwable) {
                e.printStackTrace()
//                when (e) {
//                    is IOException -> {
//                        _cocktailResponseLiveData.postValue(
//                            ApiCallNetworkResource.Error(
//                                message =
//                                "Network Failure, please check your internet connection"
//                            )
//                        )
//                    }
//                    is NullPointerException -> {
//                        _cocktailResponseLiveData.postValue(
//                            ApiCallNetworkResource.Error(
//                                "You cannot complete this transaction as your balance is not sufficient"
//                            )
//                        )
//                    }
//                    else -> {
//                        _cocktailResponseLiveData.postValue(
//                            ApiCallNetworkResource.Error(
//                                message =
//                                "an error occur please try again later"
//                            )
//                        )
//                    }
//                }
            }
        }
    }


}
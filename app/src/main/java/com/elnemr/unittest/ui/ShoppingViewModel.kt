package com.elnemr.unittest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.elnemr.unittest.data.local.ShoppingItem
import com.elnemr.unittest.data.remote.response.ImageResponse
import com.elnemr.unittest.repo.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {

    val shoppingItems = repo.observeAllShoppingItems()
    val totalPrice = repo.observeTotalPrice()

    private val _insertShoppingItemStatus = MutableLiveData<Resource<ShoppingItem>>()
    val insertShoppingItemStatus: LiveData<Resource<ShoppingItem>> = _insertShoppingItemStatus

    private val _images = MutableLiveData<Resource<ImageResponse>>()
    val images: LiveData<Resource<ImageResponse>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageURl: LiveData<String> = _curImageUrl

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repo.deleteShoppingItem(shoppingItem)
    }

    private fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repo.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String){

    }

    fun searchForImages(imageQuery: String){

    }

}
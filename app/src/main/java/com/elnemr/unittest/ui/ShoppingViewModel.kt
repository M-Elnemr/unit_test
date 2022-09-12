package com.elnemr.unittest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elnemr.unittest.data.local.ShoppingItem
import com.elnemr.unittest.data.remote.response.ImageResponse
import com.elnemr.unittest.repo.IRepo
import com.elnemr.unittest.util.Constants
import com.elnemr.unittest.util.Resource
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

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {

        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Resource.error("empty fields"))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Resource.error("name is long"))
            return
        }
        if (amountString.length > Constants.MAX_AMOUNT_LENGTH) {
            _insertShoppingItemStatus.postValue(Resource.error("amount is long"))
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Resource.error("price is long"))
            return
        }

        val shoppingItem = ShoppingItem(
            name,
            amountString.toInt(),
            priceString.toFloat(),
            _curImageUrl.value ?: ""
        )

        _insertShoppingItemStatus.postValue(
            Resource.success(
                shoppingItem
            )
        )

        insertShoppingItemIntoDb(shoppingItem)

        setCurImageUrl("")
    }

    fun searchForImages(imageQuery: String) {

        if (imageQuery.isEmpty()) return
        _images.value = Resource.loading()

        viewModelScope.launch {
            val result = repo.searchForImage(imageQuery)
            _images.value = result
        }
    }

}
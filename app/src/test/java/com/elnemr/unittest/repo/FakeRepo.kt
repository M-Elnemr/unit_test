package com.elnemr.unittest.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elnemr.unittest.data.local.ShoppingItem
import com.elnemr.unittest.data.remote.response.ImageResponse
import com.elnemr.unittest.util.Resource

// this class to test viewModel - we don't want to test the viewModel on an actual network calls
// that would make the test slow

class FakeRepo : IRepo {

    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float =
        shoppingItems.sumOf {
            it.price.toDouble()
        }.toFloat()

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> = observableShoppingItems

    override fun observeTotalPrice(): LiveData<Float> = observableTotalPrice

    override suspend fun searchForImage(searchQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) Resource.error("error", null)
        else Resource.success(ImageResponse(listOf(), 0, 0))
    }
}
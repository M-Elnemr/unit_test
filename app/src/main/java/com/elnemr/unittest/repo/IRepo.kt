package com.elnemr.unittest.repo

import androidx.lifecycle.LiveData
import com.elnemr.unittest.data.local.ShoppingItem
import com.elnemr.unittest.data.remote.response.ImageResponse
import com.elnemr.unittest.util.Resource

interface IRepo {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(searchQuery: String): Resource<ImageResponse>
}
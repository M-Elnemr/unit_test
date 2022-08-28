package com.elnemr.unittest.repo

import androidx.lifecycle.LiveData
import com.elnemr.unittest.data.local.ShoppingDao
import com.elnemr.unittest.data.local.ShoppingItem
import com.elnemr.unittest.data.remote.ApiInterface
import com.elnemr.unittest.data.remote.response.ImageResponse
import com.elnemr.unittest.util.Resource
import retrofit2.Response
import javax.inject.Inject

class Repo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val shoppingDao: ShoppingDao
) : IRepo {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) =
        shoppingDao.insertShoppingItem(shoppingItem)

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) =
        shoppingDao.deleteShoppingItem(shoppingItem)

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> =
        shoppingDao.observeAllShoppingItems()

    override fun observeTotalPrice(): LiveData<Float> =
        shoppingDao.observeTotalPrice()

    override suspend fun searchForImage(searchQuery: String): Resource<ImageResponse> {

        return try {
            val response = apiInterface.searchForImage(searchQuery)

            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("unknown error", null)
            }else Resource.error("unknown error", null)

        }catch (e: Exception){
            Resource.error(e.message.toString(), null)
        }

    }
}
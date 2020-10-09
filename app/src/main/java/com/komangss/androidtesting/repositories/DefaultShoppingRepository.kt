package com.komangss.androidtesting.repositories

import androidx.lifecycle.LiveData
import com.komangss.androidtesting.data.local.ShoppingDao
import com.komangss.androidtesting.data.local.ShoppingItem
import com.komangss.androidtesting.data.remote.PixabayAPI
import com.komangss.androidtesting.data.remote.responses.ImageResponse
import com.komangss.androidtesting.other.Resource
import javax.inject.Inject


class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred!", null)
            } else {
                Resource.error("An unknown error occurred!", null)
            }
        } catch (exception : Exception) {
            Resource.error("Couldn't reach the server. check your internet connection", null)
        }
    }

}
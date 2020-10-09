package com.komangss.androidtesting.repositories

import androidx.lifecycle.LiveData
import com.komangss.androidtesting.data.local.ShoppingItem
import com.komangss.androidtesting.data.remote.responses.ImageResponse
import com.komangss.androidtesting.other.Resource

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems() : LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchForImage(imageQuery : String) : Resource<ImageResponse>
}
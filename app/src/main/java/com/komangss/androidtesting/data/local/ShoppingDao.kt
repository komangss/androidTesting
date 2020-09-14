package com.komangss.androidtesting.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

//    not use suspend because this function return liveData, since ;iveData is already asynchronous
//    by default!
    @Query("SELECT * FROM shopping_items")
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT SUM(price * amount) FROM shopping_items")
    fun observeTotalPrice(): LiveData<Float>
}
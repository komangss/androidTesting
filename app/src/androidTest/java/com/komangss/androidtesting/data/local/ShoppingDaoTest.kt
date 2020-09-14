package com.komangss.androidtesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.komangss.androidtesting.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// run on android emulator (not in JVM), because this is instrumented test, because we need context
@RunWith(AndroidJUnit4::class)
// tell JUnit what we write here is unit test
@SmallTest
@ExperimentalCoroutinesApi
class ShoppingDaoTest {

    private lateinit var database : ShoppingItemDatabase
    private lateinit var dao : ShoppingDao

    @get:Rule
    var instantExecRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database =
            // Make this testing not using the real database and only used in RAM.
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ShoppingItemDatabase::class.java
            ).allowMainThreadQueries().build()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertShoppingItem() =
//        we don't want this execute on different thread
        runBlockingTest { // runBLockTest is optimized for testing
            val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)
            dao.insertShoppingItem(shoppingItem)

            val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

            assertThat(allShoppingItems).contains(shoppingItem)
        }


    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 0, 100f, "url", id = 3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }
}
package com.komangss.androidtesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.komangss.androidtesting.getOrAwaitValue
import com.komangss.androidtesting.launchFragmentInHiltContainer
import com.komangss.androidtesting.ui.ShoppingFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

// run on android emulator (not in JVM), because this is instrumented test, because we need context
@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ShoppingDaoTest {

//    rule for hilt
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("shopping_test_database") // because we use hilt in real app database we use named annotation
    lateinit var database : ShoppingItemDatabase
    private lateinit var dao : ShoppingDao

    @get:Rule
    var instantExecRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer() {
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
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
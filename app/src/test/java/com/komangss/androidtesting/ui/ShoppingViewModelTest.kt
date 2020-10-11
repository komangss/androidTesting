package com.komangss.androidtesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.komangss.androidtesting.MainCoroutineRule
import com.komangss.androidtesting.getOrAwaitValueTest
import com.komangss.androidtesting.other.Constants
import com.komangss.androidtesting.other.Status
import com.komangss.androidtesting.repositories.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

//    remove Exception in thread "main" java.lang.IllegalStateException: Module with the Main dispatcher had failed to initialize. For tests Dispatchers.setMain from kotlinx-coroutines-test module can be used
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingViewModel: ShoppingViewModel

    @Before
    fun setUp() {
//        We use FakeRepository because making api and dao just takes times!
//        and unit test should be fast!
        shoppingViewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, return error`() {
        shoppingViewModel.insertShoppingItem("name", "", "3.0")
        val value =
            shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too long name, return error`() {
        val stringWithSizeMoreThanMaxNameLength = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH.toInt() + 1) {
                append(i)
            }
        }
        shoppingViewModel.insertShoppingItem(
            stringWithSizeMoreThanMaxNameLength,
            "5",
            "3.0"
        )
        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too long price, return error`() {
        val stringWithSizeMoreThanMaxPriceLength = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH.toInt() + 1) {
                append(i)
            }
        }
        shoppingViewModel.insertShoppingItem(
            "name",
            "5",
            stringWithSizeMoreThanMaxPriceLength
        )
        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, return error`() {
        shoppingViewModel.insertShoppingItem(
            "name",
            "9999999999999999999999",
            "3.0"
        )
        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, return success`() {
        shoppingViewModel.insertShoppingItem(
            "name",
            "5",
            "3.0"
        )
        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}
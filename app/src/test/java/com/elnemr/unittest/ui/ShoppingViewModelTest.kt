package com.elnemr.unittest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elnemr.unittest.MainCoroutineRule
import com.elnemr.unittest.getOrAwaitValueTest
import com.elnemr.unittest.repo.FakeRepo
import com.elnemr.unittest.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    // every test will run at the same thread one after another
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeRepo())
    }

    @Test
    fun `insert shopping item with empty field, return error`() {

        viewModel.insertShoppingItem("name", "", "10.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assert(value.status == Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, return error`() {

        viewModel.insertShoppingItem("nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "3", "10.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assert(value.status == Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, return error`() {

        viewModel.insertShoppingItem("name", "3", "10000000000.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assert(value.status == Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, return error`() {

        viewModel.insertShoppingItem("name", "300000000000000", "10.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assert(value.status == Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, return success`() {

        viewModel.insertShoppingItem("name", "30", "10.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assert(value.status == Status.SUCCESS)
    }

}
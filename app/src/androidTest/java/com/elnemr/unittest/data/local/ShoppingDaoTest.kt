package com.elnemr.unittest.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.elnemr.unittest.HiltTestRunner
import com.elnemr.unittest.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class) // not needed as we are using HiltRunner
@SmallTest // for unit test
// @MediumTest // for integrated test
// @LargeTest // for UI test
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db") // to find the exact dependency
    lateinit var database: MainDatabase

    private lateinit var dao: ShoppingDao

    @Before
    fun setUp() {
        hiltRule.inject()
        // not a real database - holds the records in ram for only that test case not in the storage
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(), MainDatabase::class.java
//        )// we need to access the database from the main thread we don't want multi threading
//            .allowMainThreadQueries().build()

        // we won't inject dao because we want to init it before each test
        dao = database.shoppingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() =
        // we use run blocking because we don't want multi threading
        runTest {

            val shoppingItem = ShoppingItem("name", 3, 50f, "url", id = 1)
            dao.insertShoppingItem(shoppingItem)


            val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

            assert(allShoppingItems.contains(shoppingItem))
        }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name", 3, 50f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assert(!allShoppingItems.contains(shoppingItem))
    }

    @Test
    fun observeTotalPriceSum() = runTest {
        val shoppingItem = ShoppingItem("name", 3, 50f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 1, 20f, "url", id = 2)
        dao.insertShoppingItem(shoppingItem)
        dao.insertShoppingItem(shoppingItem2)

        val totalPrice = dao.observeTotalPrice().getOrAwaitValue()

        assert(totalPrice == 170f)
    }
}
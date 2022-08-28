package com.elnemr.unittest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 1)
abstract class MainDatabase : RoomDatabase(){

    abstract fun shoppingDao(): ShoppingDao
}
package com.elnemr.unittest.di

import android.content.Context
import androidx.room.Room
import com.elnemr.unittest.data.local.MainDatabase
import com.elnemr.unittest.data.local.ShoppingDao
import com.elnemr.unittest.data.remote.ApiInterface
import com.elnemr.unittest.repo.IRepo
import com.elnemr.unittest.repo.Repo
import com.elnemr.unittest.util.Constants.BASE_URL
import com.elnemr.unittest.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MainDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideRepo(apiInterface: ApiInterface, dao: ShoppingDao) = Repo(apiInterface, dao) as IRepo

    @Provides
    @Singleton
    fun provideShoppingDao(database: MainDatabase) = database.shoppingDao()

    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(ApiInterface::class.java)

}
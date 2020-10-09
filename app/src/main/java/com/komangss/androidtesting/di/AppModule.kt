package com.komangss.androidtesting.di

import android.content.Context
import androidx.room.Room
import com.komangss.androidtesting.data.local.ShoppingDao
import com.komangss.androidtesting.data.local.ShoppingItemDatabase
import com.komangss.androidtesting.data.remote.PixabayAPI
import com.komangss.androidtesting.other.Constants.BASE_URL
import com.komangss.androidtesting.other.Constants.DATABASE_NAME
import com.komangss.androidtesting.repositories.DefaultShoppingRepository
import com.komangss.androidtesting.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context : Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultRepository(
        dao : ShoppingDao,
        api : PixabayAPI
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        database : ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi() : PixabayAPI {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

}
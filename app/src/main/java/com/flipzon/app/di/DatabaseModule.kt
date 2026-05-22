package com.flipzon.app.di

import android.content.Context
import androidx.room.Room
import com.flipzon.app.database.AppDatabase
import com.flipzon.app.database.CartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "flipzon_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): com.flipzon.app.datastore.SessionManager = 
        com.flipzon.app.datastore.SessionManager(context)
}

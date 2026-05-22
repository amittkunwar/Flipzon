package com.flipzon.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flipzon.app.model.Product

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}

package com.flipzon.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.flipzon.app.database.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cartentity")
    fun getAll(): Flow<List<CartEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)
    @Update
    suspend fun update(item: CartEntity)
    @Delete
    suspend fun delete(item: CartEntity)
    @Query("DELETE FROM cartentity")
    suspend fun clearAll()
}

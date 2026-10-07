package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Warehouse
@Dao
interface WarehouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(w: Warehouse): Long
    @Update suspend fun update(w: Warehouse)
    @Delete suspend fun delete(w: Warehouse)
    @Query("SELECT * FROM entrepot ORDER BY ref") fun observeAll(): Flow<List<Warehouse>>
}

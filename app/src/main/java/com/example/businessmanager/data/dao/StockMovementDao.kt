package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Stock movements (llx_stock_mouvement)
@Dao
interface StockMovementDao {
    @Insert suspend fun insert(m: StockMovement): Long
    @Delete suspend fun delete(m: StockMovement)

    @Query("SELECT * FROM stock_mouvement WHERE fk_product = :productId ORDER BY datem DESC")
    fun observeByProduct(productId: Long): Flow<List<StockMovement>>

    @Query("SELECT * FROM stock_mouvement WHERE fk_entrepot = :warehouseId ORDER BY datem DESC")
    fun observeByWarehouse(warehouseId: Long): Flow<List<StockMovement>>

    @Query("SELECT * FROM stock_mouvement ORDER BY datem DESC LIMIT :limit")
    fun observeRecent(limit: Int = 100): Flow<List<StockMovement>>

    @Query("SELECT * FROM stock_mouvement WHERE origintype = :originType AND fk_origin = :originId")
    suspend fun getByOrigin(originType: String, originId: Long): List<StockMovement>
}

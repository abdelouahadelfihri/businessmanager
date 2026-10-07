package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Stock per product per warehouse (llx_product_stock)
@Dao
interface ProductStockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(s: ProductStock): Long
    @Update suspend fun update(s: ProductStock)
    @Delete suspend fun delete(s: ProductStock)

    @Query("SELECT * FROM product_stock WHERE fk_product = :productId AND fk_entrepot = :warehouseId")
    suspend fun get(productId: Long, warehouseId: Long): ProductStock?

    @Query("SELECT * FROM product_stock WHERE fk_product = :productId")
    fun observeByProduct(productId: Long): Flow<List<ProductStock>>

    @Query("SELECT * FROM product_stock WHERE fk_entrepot = :warehouseId")
    fun observeByWarehouse(warehouseId: Long): Flow<List<ProductStock>>

    @Query("SELECT COALESCE(SUM(reel),0) FROM product_stock WHERE fk_product = :productId")
    suspend fun totalForProduct(productId: Long): Double

    @Query("UPDATE product_stock SET reel = reel + :delta WHERE fk_product = :productId AND fk_entrepot = :warehouseId")
    suspend fun adjust(productId: Long, warehouseId: Long, delta: Double): Int
}

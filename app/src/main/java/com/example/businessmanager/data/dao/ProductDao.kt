package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Product
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(p: Product): Long
    @Update suspend fun update(p: Product)
    @Delete suspend fun delete(p: Product)

    @Query("SELECT * FROM product WHERE id = :id") suspend fun getById(id: Long): Product?
    @Query("SELECT * FROM product WHERE barcode = :code LIMIT 1") suspend fun getByBarcode(code: String): Product?
    @Query("SELECT * FROM product ORDER BY label") fun observeAll(): Flow<List<Product>>
    @Query("SELECT * FROM product WHERE label LIKE '%' || :q || '%' OR ref LIKE '%' || :q || '%' ORDER BY label")
    fun search(q: String): Flow<List<Product>>
    @Query("SELECT * FROM product WHERE fk_product_type = 0 AND stock <= seuil_stock_alerte ORDER BY stock")
    fun observeLowStock(): Flow<List<Product>>

    @Query("UPDATE product SET stock = stock + :delta WHERE id = :productId")
    suspend fun adjustStock(productId: Long, delta: Double)
}

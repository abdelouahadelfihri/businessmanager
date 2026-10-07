package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Supplier order lines (llx_commande_fournisseurdet)
@Dao
interface SupplierOrderLineDao {
    @Insert suspend fun insert(l: SupplierOrderLine): Long
    @Insert suspend fun insertAll(lines: List<SupplierOrderLine>)
    @Update suspend fun update(l: SupplierOrderLine)
    @Delete suspend fun delete(l: SupplierOrderLine)

    @Query("SELECT * FROM commande_fournisseurdet WHERE id = :id") suspend fun getById(id: Long): SupplierOrderLine?
    @Query("SELECT * FROM commande_fournisseurdet WHERE fk_commande = :orderId ORDER BY rang")
    fun observeByOrder(orderId: Long): Flow<List<SupplierOrderLine>>
    @Query("DELETE FROM commande_fournisseurdet WHERE fk_commande = :orderId") suspend fun deleteByOrder(orderId: Long)

    @Query("SELECT COALESCE(SUM(qty),0) FROM commande_fournisseurdet WHERE fk_product = :productId")
    suspend fun totalQtyOrdered(productId: Long): Double
}

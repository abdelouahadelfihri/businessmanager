package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Supplier invoice lines (llx_facture_fourn_det)
@Dao
interface SupplierInvoiceLineDao {
    @Insert suspend fun insert(l: SupplierInvoiceLine): Long
    @Insert suspend fun insertAll(lines: List<SupplierInvoiceLine>)
    @Update suspend fun update(l: SupplierInvoiceLine)
    @Delete suspend fun delete(l: SupplierInvoiceLine)

    @Query("SELECT * FROM facture_fourn_det WHERE id = :id") suspend fun getById(id: Long): SupplierInvoiceLine?
    @Query("SELECT * FROM facture_fourn_det WHERE fk_facture_fourn = :invoiceId")
    fun observeByInvoice(invoiceId: Long): Flow<List<SupplierInvoiceLine>>
    @Query("DELETE FROM facture_fourn_det WHERE fk_facture_fourn = :invoiceId") suspend fun deleteByInvoice(invoiceId: Long)

    @Query("SELECT COALESCE(SUM(qty),0) FROM facture_fourn_det WHERE fk_product = :productId")
    suspend fun totalQtyPurchased(productId: Long): Double
}

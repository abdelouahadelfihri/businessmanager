package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Customer invoice lines (llx_facturedet)
@Dao
interface InvoiceLineDao {
    @Insert suspend fun insert(l: InvoiceLine): Long
    @Insert suspend fun insertAll(lines: List<InvoiceLine>)
    @Update suspend fun update(l: InvoiceLine)
    @Delete suspend fun delete(l: InvoiceLine)

    @Query("SELECT * FROM facturedet WHERE id = :id") suspend fun getById(id: Long): InvoiceLine?
    @Query("SELECT * FROM facturedet WHERE fk_facture = :invoiceId ORDER BY rang")
    fun observeByInvoice(invoiceId: Long): Flow<List<InvoiceLine>>
    @Query("DELETE FROM facturedet WHERE fk_facture = :invoiceId") suspend fun deleteByInvoice(invoiceId: Long)

    @Query("SELECT COALESCE(SUM(qty),0) FROM facturedet WHERE fk_product = :productId")
    suspend fun totalQtySold(productId: Long): Double
}

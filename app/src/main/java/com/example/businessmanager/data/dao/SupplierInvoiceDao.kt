package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Supplier invoices
@Dao
interface SupplierInvoiceDao {
    @Insert suspend fun insert(i: SupplierInvoice): Long
    @Insert suspend fun insertLines(lines: List<SupplierInvoiceLine>)
    @Update suspend fun update(i: SupplierInvoice)
    @Delete suspend fun delete(i: SupplierInvoice)
    @Query("DELETE FROM facture_fourn_det WHERE fk_facture_fourn = :invoiceId") suspend fun deleteLines(invoiceId: Long)

    @Transaction
    suspend fun saveWithLines(i: SupplierInvoice, lines: List<SupplierInvoiceLine>): Long {
        val id = if (i.id == 0L) insert(i) else { update(i); deleteLines(i.id); i.id }
        insertLines(lines.map { it.copy(id = 0, invoiceId = id) })
        return id
    }

    @Transaction @Query("SELECT * FROM facture_fourn WHERE id = :id") suspend fun getWithLines(id: Long): SupplierInvoiceWithLines?
    @Query("SELECT * FROM facture_fourn ORDER BY datef DESC") fun observeAll(): Flow<List<SupplierInvoice>>
    @Query("SELECT * FROM facture_fourn WHERE fk_statut = 1 AND paye = 0 ORDER BY date_lim_reglement")
    fun observeUnpaid(): Flow<List<SupplierInvoice>>
    @Query("SELECT COALESCE(SUM(total_ttc),0) FROM facture_fourn WHERE fk_statut IN (1,2) AND datef BETWEEN :from AND :to")
    fun observePurchases(from: Long, to: Long): Flow<Double>
    @Query("UPDATE facture_fourn SET paye = :paid, fk_statut = :status WHERE id = :id")
    suspend fun setPaid(id: Long, paid: Int, status: Int)
}

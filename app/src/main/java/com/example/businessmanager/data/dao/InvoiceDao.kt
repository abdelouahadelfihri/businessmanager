package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Customer invoices
@Dao
interface InvoiceDao {
    @Insert suspend fun insert(i: Invoice): Long
    @Insert suspend fun insertLines(lines: List<InvoiceLine>)
    @Update suspend fun update(i: Invoice)
    @Delete suspend fun delete(i: Invoice)
    @Query("DELETE FROM facturedet WHERE fk_facture = :invoiceId") suspend fun deleteLines(invoiceId: Long)

    @Transaction
    suspend fun saveWithLines(i: Invoice, lines: List<InvoiceLine>): Long {
        val id = if (i.id == 0L) insert(i) else { update(i); deleteLines(i.id); i.id }
        insertLines(lines.map { it.copy(id = 0, invoiceId = id) })
        return id
    }

    @Transaction @Query("SELECT * FROM facture WHERE id = :id") suspend fun getWithLines(id: Long): InvoiceWithLines?
    @Transaction @Query("SELECT * FROM facture WHERE id = :id") fun observeWithLines(id: Long): Flow<InvoiceWithLines?>

    @Query("SELECT * FROM facture ORDER BY datef DESC") fun observeAll(): Flow<List<Invoice>>
    @Query("SELECT * FROM facture WHERE fk_soc = :thirdPartyId ORDER BY datef DESC")
    fun observeByThirdParty(thirdPartyId: Long): Flow<List<Invoice>>
    @Query("SELECT * FROM facture WHERE fk_statut = 1 AND paye = 0 ORDER BY date_lim_reglement")
    fun observeUnpaid(): Flow<List<Invoice>>
    @Query("SELECT COUNT(*) FROM facture") suspend fun count(): Int
    @Query("SELECT COALESCE(SUM(total_ttc),0) FROM facture WHERE fk_statut IN (1,2) AND datef BETWEEN :from AND :to")
    fun observeSales(from: Long, to: Long): Flow<Double>
    @Query("UPDATE facture SET paye = :paid, fk_statut = :status WHERE id = :id")
    suspend fun setPaid(id: Long, paid: Int, status: Int)
}

package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Payment-to-invoice allocations (llx_paiement_facture / llx_paiementfourn_facturefourn)
@Dao
interface PaymentAllocationDao {
    @Insert suspend fun insert(a: PaymentAllocation): Long
    @Insert suspend fun insertAll(a: List<PaymentAllocation>)
    @Update suspend fun update(a: PaymentAllocation)
    @Delete suspend fun delete(a: PaymentAllocation)

    @Query("SELECT * FROM paiement_facture WHERE fk_paiement = :paymentId")
    suspend fun getByPayment(paymentId: Long): List<PaymentAllocation>

    @Query("SELECT * FROM paiement_facture WHERE invoice_kind = :kind AND fk_facture = :invoiceId")
    fun observeByInvoice(kind: String, invoiceId: Long): Flow<List<PaymentAllocation>>

    @Query("SELECT COALESCE(SUM(amount),0) FROM paiement_facture WHERE invoice_kind = :kind AND fk_facture = :invoiceId")
    suspend fun totalPaid(kind: String, invoiceId: Long): Double
}

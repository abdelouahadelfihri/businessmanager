package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Payments
@Dao
interface PaymentDao {
    @Insert suspend fun insert(p: Payment): Long
    // Needed here so addPayment() can run in a single transaction
    @Insert suspend fun insertAllocations(a: List<PaymentAllocation>)
    @Update suspend fun update(p: Payment)
    @Delete suspend fun delete(p: Payment)
    @Query("SELECT * FROM paiement WHERE id = :id") suspend fun getById(id: Long): Payment?

    @Query("SELECT * FROM paiement ORDER BY datep DESC") fun observeAll(): Flow<List<Payment>>
    @Query("SELECT * FROM paiement WHERE direction = :direction ORDER BY datep DESC")
    fun observeByDirection(direction: String): Flow<List<Payment>>

    @Transaction
    suspend fun addPayment(p: Payment, allocations: List<PaymentAllocation>): Long {
        val id = insert(p)
        insertAllocations(allocations.map { it.copy(id = 0, paymentId = id) })
        return id
    }
}

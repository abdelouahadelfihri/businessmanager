package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Customer orders (llx_commande)
@Dao
interface CustomerOrderDao {
    @Insert suspend fun insert(o: CustomerOrder): Long
    @Update suspend fun update(o: CustomerOrder)
    @Delete suspend fun delete(o: CustomerOrder)

    // Duplicated on purpose: a @Transaction method can only call methods of its own DAO
    @Insert suspend fun insertLines(lines: List<CustomerOrderLine>)
    @Query("DELETE FROM commandedet WHERE fk_commande = :orderId") suspend fun deleteLines(orderId: Long)

    @Transaction
    suspend fun saveWithLines(o: CustomerOrder, lines: List<CustomerOrderLine>): Long {
        val id = if (o.id == 0L) insert(o) else { update(o); deleteLines(o.id); o.id }
        insertLines(lines.map { it.copy(id = 0, orderId = id) })
        return id
    }

    @Query("SELECT * FROM commande WHERE id = :id") suspend fun getById(id: Long): CustomerOrder?
    @Transaction @Query("SELECT * FROM commande WHERE id = :id") suspend fun getWithLines(id: Long): CustomerOrderWithLines?
    @Transaction @Query("SELECT * FROM commande WHERE id = :id") fun observeWithLines(id: Long): Flow<CustomerOrderWithLines?>

    @Query("SELECT * FROM commande ORDER BY date_commande DESC") fun observeAll(): Flow<List<CustomerOrder>>
    @Query("SELECT * FROM commande WHERE fk_soc = :thirdPartyId ORDER BY date_commande DESC")
    fun observeByThirdParty(thirdPartyId: Long): Flow<List<CustomerOrder>>
    @Query("SELECT * FROM commande WHERE fk_statut = :status ORDER BY date_commande DESC")
    fun observeByStatus(status: Int): Flow<List<CustomerOrder>>
    @Query("SELECT * FROM commande WHERE fk_statut >= 1 AND facture = 0 ORDER BY date_commande")
    fun observeToInvoice(): Flow<List<CustomerOrder>>
    @Query("SELECT COUNT(*) FROM commande") suspend fun count(): Int
    @Query("UPDATE commande SET fk_statut = :status WHERE id = :id") suspend fun setStatus(id: Long, status: Int)
    @Query("UPDATE commande SET facture = :billed WHERE id = :id") suspend fun setBilled(id: Long, billed: Int)
}

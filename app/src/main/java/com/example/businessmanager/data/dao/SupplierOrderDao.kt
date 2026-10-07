package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Supplier orders (llx_commande_fournisseur)
@Dao
interface SupplierOrderDao {
    @Insert suspend fun insert(o: SupplierOrder): Long
    @Update suspend fun update(o: SupplierOrder)
    @Delete suspend fun delete(o: SupplierOrder)

    // Duplicated on purpose: a @Transaction method can only call methods of its own DAO
    @Insert suspend fun insertLines(lines: List<SupplierOrderLine>)
    @Query("DELETE FROM commande_fournisseurdet WHERE fk_commande = :orderId") suspend fun deleteLines(orderId: Long)

    @Transaction
    suspend fun saveWithLines(o: SupplierOrder, lines: List<SupplierOrderLine>): Long {
        val id = if (o.id == 0L) insert(o) else { update(o); deleteLines(o.id); o.id }
        insertLines(lines.map { it.copy(id = 0, orderId = id) })
        return id
    }

    @Query("SELECT * FROM commande_fournisseur WHERE id = :id") suspend fun getById(id: Long): SupplierOrder?
    @Transaction @Query("SELECT * FROM commande_fournisseur WHERE id = :id") suspend fun getWithLines(id: Long): SupplierOrderWithLines?
    @Transaction @Query("SELECT * FROM commande_fournisseur WHERE id = :id") fun observeWithLines(id: Long): Flow<SupplierOrderWithLines?>

    @Query("SELECT * FROM commande_fournisseur ORDER BY date_commande DESC") fun observeAll(): Flow<List<SupplierOrder>>
    @Query("SELECT * FROM commande_fournisseur WHERE fk_soc = :thirdPartyId ORDER BY date_commande DESC")
    fun observeByThirdParty(thirdPartyId: Long): Flow<List<SupplierOrder>>
    @Query("SELECT * FROM commande_fournisseur WHERE fk_statut = :status ORDER BY date_commande DESC")
    fun observeByStatus(status: Int): Flow<List<SupplierOrder>>
    @Query("SELECT * FROM commande_fournisseur WHERE fk_statut IN (1,2,3,4) ORDER BY date_livraison")
    fun observePendingReception(): Flow<List<SupplierOrder>>
    @Query("SELECT COUNT(*) FROM commande_fournisseur") suspend fun count(): Int
    @Query("UPDATE commande_fournisseur SET fk_statut = :status WHERE id = :id") suspend fun setStatus(id: Long, status: Int)
}

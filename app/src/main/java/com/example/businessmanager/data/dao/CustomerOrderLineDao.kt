package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Customer order lines (llx_commandedet)
@Dao
interface CustomerOrderLineDao {
    @Insert suspend fun insert(l: CustomerOrderLine): Long
    @Insert suspend fun insertAll(lines: List<CustomerOrderLine>)
    @Update suspend fun update(l: CustomerOrderLine)
    @Delete suspend fun delete(l: CustomerOrderLine)

    @Query("SELECT * FROM commandedet WHERE id = :id") suspend fun getById(id: Long): CustomerOrderLine?
    @Query("SELECT * FROM commandedet WHERE fk_commande = :orderId ORDER BY rang")
    fun observeByOrder(orderId: Long): Flow<List<CustomerOrderLine>>
    @Query("DELETE FROM commandedet WHERE fk_commande = :orderId") suspend fun deleteByOrder(orderId: Long)
}

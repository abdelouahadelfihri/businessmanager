package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Customer quotes (llx_propal)
@Dao
interface QuoteDao {
    @Insert suspend fun insert(q: Quote): Long
    @Update suspend fun update(q: Quote)
    @Delete suspend fun delete(q: Quote)

    // Duplicated on purpose: a @Transaction method can only call methods of its own DAO
    @Insert suspend fun insertLines(lines: List<QuoteLine>)
    @Query("DELETE FROM propaldet WHERE fk_propal = :quoteId") suspend fun deleteLines(quoteId: Long)

    @Transaction
    suspend fun saveWithLines(q: Quote, lines: List<QuoteLine>): Long {
        val id = if (q.id == 0L) insert(q) else { update(q); deleteLines(q.id); q.id }
        insertLines(lines.map { it.copy(id = 0, quoteId = id) })
        return id
    }

    @Query("SELECT * FROM propal WHERE id = :id") suspend fun getById(id: Long): Quote?
    @Transaction @Query("SELECT * FROM propal WHERE id = :id") suspend fun getWithLines(id: Long): QuoteWithLines?
    @Transaction @Query("SELECT * FROM propal WHERE id = :id") fun observeWithLines(id: Long): Flow<QuoteWithLines?>

    @Query("SELECT * FROM propal ORDER BY datep DESC") fun observeAll(): Flow<List<Quote>>
    @Query("SELECT * FROM propal WHERE fk_soc = :thirdPartyId ORDER BY datep DESC")
    fun observeByThirdParty(thirdPartyId: Long): Flow<List<Quote>>
    @Query("SELECT * FROM propal WHERE fk_statut = :status ORDER BY datep DESC")
    fun observeByStatus(status: Int): Flow<List<Quote>>
    @Query("SELECT COUNT(*) FROM propal") suspend fun count(): Int
    @Query("UPDATE propal SET fk_statut = :status WHERE id = :id") suspend fun setStatus(id: Long, status: Int)
}

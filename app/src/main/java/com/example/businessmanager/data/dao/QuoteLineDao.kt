package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Quote lines (llx_propaldet)
@Dao
interface QuoteLineDao {
    @Insert suspend fun insert(l: QuoteLine): Long
    @Insert suspend fun insertAll(lines: List<QuoteLine>)
    @Update suspend fun update(l: QuoteLine)
    @Delete suspend fun delete(l: QuoteLine)

    @Query("SELECT * FROM propaldet WHERE id = :id") suspend fun getById(id: Long): QuoteLine?
    @Query("SELECT * FROM propaldet WHERE fk_propal = :quoteId ORDER BY rang")
    fun observeByQuote(quoteId: Long): Flow<List<QuoteLine>>
    @Query("DELETE FROM propaldet WHERE fk_propal = :quoteId") suspend fun deleteByQuote(quoteId: Long)
}

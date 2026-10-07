package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Expense lines (llx_expensereport_det)
@Dao
interface ExpenseLineDao {
    @Insert suspend fun insert(l: ExpenseLine): Long
    @Insert suspend fun insertAll(lines: List<ExpenseLine>)
    @Update suspend fun update(l: ExpenseLine)
    @Delete suspend fun delete(l: ExpenseLine)

    @Query("SELECT * FROM expensereport_det WHERE id = :id") suspend fun getById(id: Long): ExpenseLine?
    @Query("SELECT * FROM expensereport_det WHERE fk_expensereport = :reportId ORDER BY date")
    fun observeByReport(reportId: Long): Flow<List<ExpenseLine>>
    @Query("DELETE FROM expensereport_det WHERE fk_expensereport = :reportId") suspend fun deleteByReport(reportId: Long)

    @Query("SELECT fk_c_type_fees AS feeType, COALESCE(SUM(total_ttc),0) AS total FROM expensereport_det WHERE date BETWEEN :from AND :to GROUP BY fk_c_type_fees")
    fun observeTotalsByType(from: Long, to: Long): Flow<List<FeeTypeTotal>>
}

data class FeeTypeTotal(val feeType: String, val total: Double)

package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Expenses
@Dao
interface ExpenseDao {
    @Insert suspend fun insert(r: ExpenseReport): Long
    @Insert suspend fun insertLines(lines: List<ExpenseLine>)
    @Update suspend fun update(r: ExpenseReport)
    @Delete suspend fun delete(r: ExpenseReport)
    @Query("DELETE FROM expensereport_det WHERE fk_expensereport = :reportId") suspend fun deleteLines(reportId: Long)

    @Transaction
    suspend fun saveWithLines(r: ExpenseReport, lines: List<ExpenseLine>): Long {
        val id = if (r.id == 0L) insert(r) else { update(r); deleteLines(r.id); r.id }
        insertLines(lines.map { it.copy(id = 0, reportId = id) })
        return id
    }

    @Transaction @Query("SELECT * FROM expensereport WHERE id = :id") suspend fun getWithLines(id: Long): ExpenseReportWithLines?
    @Query("SELECT * FROM expensereport ORDER BY date_debut DESC") fun observeAll(): Flow<List<ExpenseReport>>
    @Query("SELECT COALESCE(SUM(total_ttc),0) FROM expensereport WHERE date_debut BETWEEN :from AND :to")
    fun observeTotal(from: Long, to: Long): Flow<Double>
}

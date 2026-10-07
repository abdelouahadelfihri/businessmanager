package com.example.bizapp.data.entity

import androidx.room.*

// llx_expensereport : expense reports
@Entity(tableName = "expensereport", indices = [Index(value = ["ref"], unique = true)])
data class ExpenseReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    @ColumnInfo(name = "date_debut") val dateStart: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date_fin") val dateEnd: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    @ColumnInfo(name = "fk_statut") val status: Int = 0         // 0 draft, 2 validated, 5 approved, 6 paid
)

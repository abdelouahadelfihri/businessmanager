package com.example.bizapp.data.entity

import androidx.room.*

// llx_expensereport_det : expense lines
@Entity(
    tableName = "expensereport_det",
    foreignKeys = [ForeignKey(ExpenseReport::class, ["id"], ["fk_expensereport"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("fk_expensereport")]
)
data class ExpenseLine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_expensereport") val reportId: Long,
    @ColumnInfo(name = "fk_c_type_fees") val feeType: String,   // TRANSPORT, MEAL, HOTEL, OTHER...
    val comments: String? = null,
    val qty: Double = 1.0,
    @ColumnInfo(name = "value_unit") val unitValue: Double = 0.0,
    @ColumnInfo(name = "tva_tx") val vatRate: Double = 0.0,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    val date: Long = System.currentTimeMillis()
)

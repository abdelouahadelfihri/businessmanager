package com.example.bizapp.data.entity

import androidx.room.*

// Room relation
data class ExpenseReportWithLines(
    @Embedded val report: ExpenseReport,
    @Relation(parentColumn = "id", entityColumn = "fk_expensereport") val lines: List<ExpenseLine>
)

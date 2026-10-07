package com.example.bizapp.data.entity

import androidx.room.*

// Room relation
data class InvoiceWithLines(
    @Embedded val invoice: Invoice,
    @Relation(parentColumn = "id", entityColumn = "fk_facture") val lines: List<InvoiceLine>
)

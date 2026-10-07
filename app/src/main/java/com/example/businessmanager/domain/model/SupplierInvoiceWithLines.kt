package com.example.bizapp.data.entity

import androidx.room.*

// Room relation
data class SupplierInvoiceWithLines(
    @Embedded val invoice: SupplierInvoice,
    @Relation(parentColumn = "id", entityColumn = "fk_facture_fourn") val lines: List<SupplierInvoiceLine>
)

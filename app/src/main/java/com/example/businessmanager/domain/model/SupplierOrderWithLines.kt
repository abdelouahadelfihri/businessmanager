package com.example.bizapp.data.entity

import androidx.room.*

// Room relation
data class SupplierOrderWithLines(
    @Embedded val order: SupplierOrder,
    @Relation(parentColumn = "id", entityColumn = "fk_commande") val lines: List<SupplierOrderLine>
)

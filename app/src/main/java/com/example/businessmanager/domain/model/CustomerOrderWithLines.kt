package com.example.bizapp.data.entity

import androidx.room.*

// Room relation
data class CustomerOrderWithLines(
    @Embedded val order: CustomerOrder,
    @Relation(parentColumn = "id", entityColumn = "fk_commande") val lines: List<CustomerOrderLine>
)

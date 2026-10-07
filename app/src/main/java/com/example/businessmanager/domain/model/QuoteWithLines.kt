package com.example.bizapp.data.entity

import androidx.room.*

// Room relation
data class QuoteWithLines(
    @Embedded val quote: Quote,
    @Relation(parentColumn = "id", entityColumn = "fk_propal") val lines: List<QuoteLine>
)

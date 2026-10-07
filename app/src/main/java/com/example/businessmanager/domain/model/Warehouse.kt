package com.example.bizapp.data.entity

import androidx.room.*

// llx_entrepot : warehouses
@Entity(tableName = "entrepot")
data class Warehouse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    val description: String? = null,
    val address: String? = null,
    val statut: Int = 1
)

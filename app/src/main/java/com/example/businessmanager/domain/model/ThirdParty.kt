package com.example.bizapp.data.entity

import androidx.room.*

// llx_societe : third parties (customers + suppliers)
@Entity(tableName = "societe", indices = [Index("nom")])
data class ThirdParty(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "nom") val name: String,
    @ColumnInfo(name = "code_client") val customerCode: String? = null,
    @ColumnInfo(name = "code_fournisseur") val supplierCode: String? = null,
    @ColumnInfo(name = "client") val isCustomer: Int = 1,      // 0 no, 1 customer, 2 prospect, 3 both
    @ColumnInfo(name = "fournisseur") val isSupplier: Int = 0, // 0/1
    val address: String? = null,
    val zip: String? = null,
    val town: String? = null,
    val phone: String? = null,
    val email: String? = null,
    @ColumnInfo(name = "tva_intra") val vatNumber: String? = null,
    @ColumnInfo(name = "idprof1") val ice: String? = null,     // ICE in Morocco
    val status: Int = 1,                                        // 1 active, 0 closed
    @ColumnInfo(name = "datec") val createdAt: Long = System.currentTimeMillis()
)

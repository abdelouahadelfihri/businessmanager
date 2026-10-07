package com.example.bizapp.data.entity

import androidx.room.*

// llx_paiement / llx_paiementfourn : payments
// Dolibarr uses two table pairs (customer / supplier). Merged here with a `direction` column.
@Entity(tableName = "paiement", indices = [Index("direction")])
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String? = null,
    val direction: String,                                      // "IN" (customer) / "OUT" (supplier)
    @ColumnInfo(name = "datep") val date: Long = System.currentTimeMillis(),
    val amount: Double,
    @ColumnInfo(name = "fk_paiement") val method: String = "CASH", // CASH, CHQ, VIR, CB...
    @ColumnInfo(name = "num_paiement") val number: String? = null, // cheque / transfer number
    val note: String? = null
)

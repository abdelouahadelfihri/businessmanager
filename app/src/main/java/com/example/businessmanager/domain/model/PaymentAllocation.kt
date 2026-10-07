package com.example.bizapp.data.entity

import androidx.room.*

// llx_paiement_facture / llx_paiementfourn_facturefourn : allocation
@Entity(
    tableName = "paiement_facture",
    foreignKeys = [ForeignKey(Payment::class, ["id"], ["fk_paiement"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("fk_paiement"), Index(value = ["invoice_kind", "fk_facture"])]
)
data class PaymentAllocation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_paiement") val paymentId: Long,
    @ColumnInfo(name = "invoice_kind") val invoiceKind: String, // "CUSTOMER" / "SUPPLIER"
    @ColumnInfo(name = "fk_facture") val invoiceId: Long,
    val amount: Double
)

package com.example.bizapp.data.entity

import androidx.room.*

// llx_facture_fourn : supplier invoices (purchases)
@Entity(
    tableName = "facture_fourn",
    foreignKeys = [ForeignKey(ThirdParty::class, ["id"], ["fk_soc"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index("fk_soc")]
)
data class SupplierInvoice(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    @ColumnInfo(name = "ref_supplier") val supplierRef: String? = null,
    @ColumnInfo(name = "fk_soc") val thirdPartyId: Long,
    @ColumnInfo(name = "datef") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date_lim_reglement") val dueDate: Long? = null,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    val paye: Int = 0,
    @ColumnInfo(name = "fk_statut") val status: Int = 0         // 0 draft, 1 validated, 2 paid, 3 abandoned
)

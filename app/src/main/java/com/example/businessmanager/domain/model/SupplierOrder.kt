package com.example.bizapp.data.entity

import androidx.room.*

// llx_commande_fournisseur : supplier (purchase) orders
// Status: 0 draft, 1 validated, 2 approved, 3 ordered, 4 partially received,
//         5 received, 6/7 canceled, 9 refused
@Entity(
    tableName = "commande_fournisseur",
    foreignKeys = [ForeignKey(ThirdParty::class, ["id"], ["fk_soc"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index(value = ["ref"], unique = true), Index("fk_soc")]
)
data class SupplierOrder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    @ColumnInfo(name = "ref_supplier") val supplierRef: String? = null,
    @ColumnInfo(name = "fk_soc") val thirdPartyId: Long,
    @ColumnInfo(name = "date_commande") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date_livraison") val expectedDeliveryDate: Long? = null,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    @ColumnInfo(name = "fk_statut") val status: Int = 0,
    @ColumnInfo(name = "note_public") val notePublic: String? = null,
    @ColumnInfo(name = "note_private") val notePrivate: String? = null
)

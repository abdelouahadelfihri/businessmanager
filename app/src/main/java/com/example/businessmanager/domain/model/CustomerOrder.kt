package com.example.bizapp.data.entity

import androidx.room.*

// llx_commande : customer orders
// Status: -1 canceled, 0 draft, 1 validated, 2 in process/shipped, 3 delivered (closed)
@Entity(
    tableName = "commande",
    foreignKeys = [ForeignKey(ThirdParty::class, ["id"], ["fk_soc"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index(value = ["ref"], unique = true), Index("fk_soc")]
)
data class CustomerOrder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    @ColumnInfo(name = "ref_client") val customerRef: String? = null,
    @ColumnInfo(name = "fk_soc") val thirdPartyId: Long,
    @ColumnInfo(name = "fk_propal") val quoteId: Long? = null,     // origin quote, if any
    @ColumnInfo(name = "date_commande") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date_livraison") val deliveryDate: Long? = null,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    @ColumnInfo(name = "facture") val billed: Int = 0,              // 0 not billed, 1 billed
    @ColumnInfo(name = "fk_statut") val status: Int = 0,
    @ColumnInfo(name = "note_public") val notePublic: String? = null,
    @ColumnInfo(name = "note_private") val notePrivate: String? = null
)

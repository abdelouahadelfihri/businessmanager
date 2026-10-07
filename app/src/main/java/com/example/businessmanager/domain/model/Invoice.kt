package com.example.bizapp.data.entity

import androidx.room.*

// llx_facture : customer invoices
@Entity(
    tableName = "facture",
    foreignKeys = [ForeignKey(ThirdParty::class, ["id"], ["fk_soc"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index(value = ["ref"], unique = true), Index("fk_soc")]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    @ColumnInfo(name = "fk_soc") val thirdPartyId: Long,
    val type: Int = 0,                                          // 0 standard, 2 credit note, 3 deposit
    @ColumnInfo(name = "datef") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date_lim_reglement") val dueDate: Long? = null,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    val paye: Int = 0,                                          // 0 unpaid, 1 paid
    @ColumnInfo(name = "fk_statut") val status: Int = 0,        // 0 draft, 1 validated, 2 paid, 3 abandoned
    @ColumnInfo(name = "note_public") val notePublic: String? = null,
    @ColumnInfo(name = "note_private") val notePrivate: String? = null
)

package com.example.bizapp.data.entity

import androidx.room.*

// llx_propal : customer quotes
// Status: 0 draft, 1 validated (open), 2 signed, 3 not signed, 4 billed
@Entity(
    tableName = "propal",
    foreignKeys = [ForeignKey(ThirdParty::class, ["id"], ["fk_soc"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index(value = ["ref"], unique = true), Index("fk_soc")]
)
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    @ColumnInfo(name = "fk_soc") val thirdPartyId: Long,
    @ColumnInfo(name = "datep") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "fin_validite") val validUntil: Long? = null,
    @ColumnInfo(name = "total_ht") val totalExclTax: Double = 0.0,
    @ColumnInfo(name = "total_tva") val totalVat: Double = 0.0,
    @ColumnInfo(name = "total_ttc") val totalInclTax: Double = 0.0,
    @ColumnInfo(name = "fk_statut") val status: Int = 0,
    @ColumnInfo(name = "note_public") val notePublic: String? = null,
    @ColumnInfo(name = "note_private") val notePrivate: String? = null
)

package com.example.bizapp.data.entity

import androidx.room.*

// llx_socpeople : contacts
@Entity(
    tableName = "socpeople",
    foreignKeys = [ForeignKey(ThirdParty::class, ["id"], ["fk_soc"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("fk_soc")]
)
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_soc") val thirdPartyId: Long? = null,
    val lastname: String,
    val firstname: String? = null,
    @ColumnInfo(name = "poste") val jobTitle: String? = null,
    val phone: String? = null,
    @ColumnInfo(name = "phone_mobile") val mobile: String? = null,
    val email: String? = null,
    val statut: Int = 1
)

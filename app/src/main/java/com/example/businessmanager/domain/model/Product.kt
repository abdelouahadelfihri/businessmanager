package com.example.bizapp.data.entity

import androidx.room.*

// llx_product : products & services
@Entity(tableName = "product", indices = [Index(value = ["ref"], unique = true)])
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ref: String,
    val label: String,
    val description: String? = null,
    @ColumnInfo(name = "fk_product_type") val type: Int = 0,   // 0 product, 1 service
    val price: Double = 0.0,                                    // selling price excl. tax
    @ColumnInfo(name = "tva_tx") val vatRate: Double = 20.0,
    @ColumnInfo(name = "cost_price") val costPrice: Double = 0.0,
    val barcode: String? = null,
    @ColumnInfo(name = "tosell") val toSell: Int = 1,
    @ColumnInfo(name = "tobuy") val toBuy: Int = 1,
    val stock: Double = 0.0,                                    // denormalized total (as in llx_product.stock)
    @ColumnInfo(name = "seuil_stock_alerte") val alertThreshold: Double = 0.0
)

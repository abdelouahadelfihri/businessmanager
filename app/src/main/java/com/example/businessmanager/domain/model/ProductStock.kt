package com.example.bizapp.data.entity

import androidx.room.*

// llx_product_stock : stock per product per warehouse
@Entity(
    tableName = "product_stock",
    foreignKeys = [
        ForeignKey(Product::class, ["id"], ["fk_product"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Warehouse::class, ["id"], ["fk_entrepot"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["fk_product", "fk_entrepot"], unique = true), Index("fk_entrepot")]
)
data class ProductStock(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_product") val productId: Long,
    @ColumnInfo(name = "fk_entrepot") val warehouseId: Long,
    @ColumnInfo(name = "reel") val quantity: Double = 0.0
)

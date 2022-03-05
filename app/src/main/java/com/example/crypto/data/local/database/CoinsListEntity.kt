package com.example.crypto.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins_list")
data class CoinsListEntity(
    @PrimaryKey val symbol: String,
    val id: String?,
    val name: String?,
    val price: String?,
    val changePercent: Double? = null,
    val image: String? = null,
    val isFavourite: Boolean = false
)

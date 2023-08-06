package com.claucas90.e2m6.MODELO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "TABLE_DATOS")
data class Datos(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "nombre")
    val nombre: String,
    @ColumnInfo(name = "precio")
    val precio: Double,
    @ColumnInfo(name = "cantidad")
    val cantidad: Int
)
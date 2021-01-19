package com.example.fairmoney.data.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class Data(
    val email: String,
    val firstName: String,
    @PrimaryKey
    val id: String,
    val lastName: String,
    val picture: String,
    val title: String,
)
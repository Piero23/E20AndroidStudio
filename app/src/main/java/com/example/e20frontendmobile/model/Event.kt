package com.example.e20frontendmobile.model

import android.graphics.Bitmap
import androidx.room.Entity
import java.util.Date

@Entity(tableName = "contacts")
class Event(val id: String,
            val description: String,
            val title: String,
            val date: Date,
            var location: String,
            val image: Bitmap? = null,
            val posti: Int,
            val prezzo: Int ) {
}
package com.example.flashcards.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deckName: String,
    val description: String?,
    val flashcards: List<Flashcard>
)
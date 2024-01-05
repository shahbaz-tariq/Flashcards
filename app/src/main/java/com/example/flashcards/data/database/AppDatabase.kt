package com.example.flashcards.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flashcards.data.database.dao.DeckDao
import com.example.flashcards.data.database.dao.FlashcardDao
import com.example.flashcards.data.model.Deck
import com.example.flashcards.data.model.Flashcard

@Database(entities = [Flashcard::class, Deck::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun deckDao(): DeckDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flashcard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.flashcards.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcards.data.model.Deck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(deck: Deck)

    @Update
    suspend fun updateDeck(deck: Deck)

    @Delete
    suspend fun deleteDeck(deck: Deck)

    @Query("SELECT * FROM decks ORDER BY id ASC")
    fun getAllDecks(): Flow<List<Deck>>

    @Query("SELECT DISTINCT deckName FROM flashcards")
    fun getAllDistinctDecks(): Flow<List<String>>

    @Query("SELECT * FROM decks WHERE id = :deckId")
    fun getDeckById(deckId: Int): Flow<Deck>

    /*@Query("DELETE FROM decks WHERE id = :deckId AND flashcardId = :flashcardId")
    suspend fun removeFlashcardFromDeck(deckId: Int, flashcardId: Int)*/

}
package com.example.flashcards.data.repositories

import androidx.lifecycle.LiveData
import com.example.flashcards.data.database.dao.DeckDao
import com.example.flashcards.data.model.Deck
import kotlinx.coroutines.flow.Flow

class DeckRepository(private val deckDao: DeckDao) {

    val allDecks: Flow<List<Deck>> = deckDao.getAllDecks()

    suspend fun insert(deck: Deck) {
        deckDao.insertDeck(deck)
    }

    suspend fun update(deck: Deck) {
        deckDao.updateDeck(deck)
    }

    suspend fun delete(deck: Deck) {
        deckDao.deleteDeck(deck)
    }

//    fun getAllDecks(): Flow<List<Deck>>{
//        return deckDao.getAllDecks()
//    }

    fun getAllDistinctDecks(): Flow<List<String>> {
        return deckDao.getAllDistinctDecks()
    }

    fun getDeckById(deckId: Int): Flow<Deck> {
        return deckDao.getDeckById(deckId)
    }

}

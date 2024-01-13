package com.example.flashcards.data.repositories

import androidx.lifecycle.LiveData
import com.example.flashcards.data.database.dao.FlashcardDao
import com.example.flashcards.data.model.Flashcard
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val flashcardDao: FlashcardDao) {

    val allFlashcards: Flow<List<Flashcard>> = flashcardDao.getAllFlashcards()

    suspend fun insert(flashcard: Flashcard) {
        flashcardDao.insertFlashcard(flashcard)
    }

    suspend fun update(flashcard: Flashcard) {
        flashcardDao.updateFlashcard(flashcard)
    }

    suspend fun delete(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }

    fun getFlashcardsByDeck(deckName: String?): Flow<List<Flashcard>> {
        return flashcardDao.getFlashcardsByDeck(deckName)
    }
}

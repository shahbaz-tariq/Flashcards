package com.example.flashcards.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.data.database.AppDatabase
import com.example.flashcards.data.model.Flashcard
import com.example.flashcards.data.repositories.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    val allFlashcards: Flow<List<Flashcard>>

    init {
        val flashcardDao = AppDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardDao)
        allFlashcards = repository.allFlashcards
    }

    suspend fun insert(flashcard: Flashcard) {
        repository.insert(flashcard)
    }

    suspend fun update(flashcard: Flashcard) {
        repository.update(flashcard)
    }

    suspend fun delete(flashcard: Flashcard) {
        repository.delete(flashcard)
    }

    fun getFlashcardsByDeck(deckName: String?): Flow<List<Flashcard>> {
        return repository.getFlashcardsByDeck(deckName)
    }
}

class FlashcardViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlashcardViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

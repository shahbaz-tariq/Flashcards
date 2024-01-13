package com.example.flashcards.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.data.database.AppDatabase
import com.example.flashcards.data.model.Deck
import com.example.flashcards.data.repositories.DeckRepository
import kotlinx.coroutines.flow.Flow

class DeckViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DeckRepository
    val allDecks: LiveData<List<Deck>>

    init {
        val deckDao = AppDatabase.getDatabase(application).deckDao()
        repository = DeckRepository(deckDao)
        allDecks = repository.allDecks.asLiveData()
    }

    suspend fun insert(deck: Deck) {
        repository.insert(deck)
    }

    suspend fun update(deck: Deck) {
        repository.update(deck)
    }

    suspend fun delete(deck: Deck) {
        repository.delete(deck)
    }

    fun getAllDistinctDecks(): Flow<List<String>> {
        return repository.getAllDistinctDecks()
    }

    fun getDeckById(deckId: Int): Flow<Deck> {
        return repository.getDeckById(deckId)
    }

}

class DeckViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeckViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
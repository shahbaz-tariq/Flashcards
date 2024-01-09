package com.example.flashcards.data.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter

class FlashcardListTypeConverter {
    @TypeConverter
    fun fromFlashcardList(flashcards: List<Flashcard>): String = Gson().toJson(flashcards)

    @TypeConverter
    fun toFlashcardList(flashcardString: String): List<Flashcard> = Gson().fromJson(flashcardString, object : TypeToken<List<Flashcard>>() {}.type)
}

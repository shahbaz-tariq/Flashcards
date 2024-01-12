package com.example.flashcards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.data.model.Flashcard
import com.example.flashcards.databinding.ItemFlashcardBinding

class FlashcardAdapter(
    private val flashcards: List<Flashcard>,
    private val listener: FlashcardAdapterListener
) :
    RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {

    interface FlashcardAdapterListener {
        fun onEditClick(flashcard: Flashcard)
        fun onDeleteClick(flashcard: Flashcard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.bind(flashcard)
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }

    inner class ViewHolder(private val binding: ItemFlashcardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(flashcards[position])
                }
            }

            binding.ivDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(flashcards[position])
                }
            }

        }
        fun bind(flashcard: Flashcard) {

            // Update UI elements with flashcard details
            binding.textQuestion.text = flashcard.question
            binding.textAnswer.text = flashcard.answer
        }
    }
}

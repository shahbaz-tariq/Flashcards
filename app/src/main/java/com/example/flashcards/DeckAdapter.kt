package com.example.flashcards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.data.model.Deck
import com.example.flashcards.databinding.ItemDeckBinding

class DeckAdapter : ListAdapter<Deck, DeckAdapter.DeckViewHolder>(DeckDiffCallback()) {

    private var onItemClickListener: ((Deck) -> Unit)? = null

    fun setOnItemClickListener(listener: (Deck) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val binding = ItemDeckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeckViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = getItem(position)
        holder.bind(deck)
    }

    inner class DeckViewHolder(private val binding: ItemDeckBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(getItem(bindingAdapterPosition))
            }
        }

        fun bind(deck: Deck) {
            binding.deckName.text = deck.deckName
            binding.cardCount.text = "${deck.cards.size} cards"
            // Bind other elements as needed, e.g., deck description or icons
        }
    }
}

class DeckDiffCallback : DiffUtil.ItemCallback<Deck>() {
    override fun areItemsTheSame(oldItem: Deck, newItem: Deck): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Deck, newItem: Deck): Boolean {
        return oldItem == newItem
    }
}
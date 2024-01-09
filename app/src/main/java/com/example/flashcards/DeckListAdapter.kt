package com.example.flashcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.data.model.Deck

class DeckListAdapter : ListAdapter<Deck, DeckListAdapter.DeckViewHolder>(DeckDiffCallback()) {

    private var onItemClickListener: ((Deck) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = getItem(position)
        holder.bind(deck)
    }

    inner class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val deckNameTextView: TextView = itemView.findViewById(R.id.deck_name)
        private val cardCountTextView: TextView = itemView.findViewById(R.id.card_count)
        private val deckDescription: TextView = itemView.findViewById(R.id.deck_description)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deck = getItem(position)
                    onItemClickListener?.invoke(deck)
                }
            }
        }

        fun bind(deck: Deck) {
            deckNameTextView.text = deck.deckName
            val cardCount = "${deck.flashcards.size} Cards"
            cardCountTextView.text = cardCount
            deckDescription.text = deck.description
        }
    }

    fun setOnItemClickListener(listener: (Deck) -> Unit) {
        onItemClickListener = listener
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

package com.example.flashcards.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.DeckListAdapter
import com.example.flashcards.databinding.FragmentHomeBinding
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.flashcards.R
import com.example.flashcards.data.model.Deck
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: DeckViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DeckViewModel::class.java]

        // Setup RecyclerView
        binding.deckList.layoutManager = LinearLayoutManager(context)
        val adapter = DeckListAdapter()
        binding.deckList.adapter = adapter

        // Observe decks and update UI
        viewModel.allDecks.observe(viewLifecycleOwner) { decks: List<Deck> ->
            adapter.submitList(decks)
        }

        // Handle FAB click for creating a new deck
        binding.fabAddDeck.setOnClickListener {
            // Implement new deck creation logic
            showAddDeckDialog()
        }

        // Handle deck item click for navigation
        adapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDeckDetailFragment(it.id)
            findNavController().navigate(action)
        }
    }

    private fun showAddDeckDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_deck, null)
        val deckNameEditText = dialogView.findViewById<EditText>(R.id.deck_name_edit_text)
        val deckDescriptionEditText = dialogView.findViewById<EditText>(R.id.deck_description_edit_text)

        val builder = AlertDialog.Builder(context)
            .setTitle("Add Deck")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val deckName = deckNameEditText.text.toString().trim()
                val deckDescription = deckDescriptionEditText.text.toString().trim()

                // Validate input (optional)
                if (deckName.isNotEmpty()) {
                    // Save deck to database
                    //viewModel.insertDeck(Deck(name = deckName, description = deckDescription))

                    lifecycleScope.launch {
                        viewModel.insert(Deck(deckName = deckName, description = deckDescription, flashcards = emptyList()))
                        // Update UI after insertion
                    }

                    // Update UI to reflect the new deck
                } else {
                    // Show error message if name is empty
                }
            }
            .setNegativeButton("Cancel", null)

        builder.show()
    }

}
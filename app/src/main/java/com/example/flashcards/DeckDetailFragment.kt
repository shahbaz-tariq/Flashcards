package com.example.flashcards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.data.model.Deck
import com.example.flashcards.databinding.FragmentDeckDetailBinding
import com.example.flashcards.ui.DeckViewModel

class DeckDetailFragment : Fragment() {

    private lateinit var binding: FragmentDeckDetailBinding
    private lateinit var viewModel: DeckViewModel
    private var deckId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            // Retrieve deckId from navigation arguments
            deckId = it.getInt("deckId") // Handle potential null value
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeckDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DeckViewModel::class.java)


        // Observe deck details and update UI
        viewModel.getDeckById(deckId).observe(viewLifecycleOwner) { deck: Deck ->
            // Update UI with deck details
            binding.textDeckName.text = deck.deckName
            binding.textDeckDescription.text = deck.description ?: ""

            // Set up RecyclerView for flashcards
            val flashcardAdapter = FlashcardAdapter(deck.flashcards) // Pass flashcards from deck
            binding.recyclerViewFlashcards.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewFlashcards.adapter = flashcardAdapter

            // Handle button clicks
            binding.btnQuizDeck.setOnClickListener {
                //findNavController().navigate(DeckDetailFragmentDirections.actionDeckDetailFragmentToQuizFragment(deckId))
            }

            binding.btnStudyDeck.setOnClickListener {
                //findNavController().navigate(DeckDetailFragmentDirections.actionDeckDetailFragmentToFlashcardStudyFragment(deckId))
            }

            // Additional button click handlers for add/edit/delete flashcards, delete deck, etc.
        }
    }
}
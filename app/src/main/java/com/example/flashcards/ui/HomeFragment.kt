package com.example.flashcards.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.DeckListAdapter
import com.example.flashcards.databinding.FragmentHomeBinding
import androidx.lifecycle.observe
import com.example.flashcards.data.model.Deck


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
        }

        // Handle deck item click for navigation
        adapter.setOnItemClickListener { /*deck ->
            val action = HomeFragmentDirections.actionHomeFragmentToDeckDetailFragment(deck.id)
            findNavController().navigate(action)*/
        }
    }

}
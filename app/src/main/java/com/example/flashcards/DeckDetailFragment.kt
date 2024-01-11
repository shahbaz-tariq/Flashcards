package com.example.flashcards

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
import com.example.flashcards.data.model.Deck
import com.example.flashcards.data.model.Flashcard
import com.example.flashcards.databinding.FragmentDeckDetailBinding
import com.example.flashcards.ui.DeckViewModel
import com.example.flashcards.ui.FlashcardViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DeckDetailFragment : Fragment() {

    private lateinit var binding: FragmentDeckDetailBinding
    private lateinit var viewModel: DeckViewModel
    private lateinit var viewModel2: FlashcardViewModel
    private lateinit var flashcardAdapter: FlashcardAdapter
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
        viewModel2 = ViewModelProvider(this).get(FlashcardViewModel::class.java)

        // Observe deck details and update UI
        lifecycleScope.launch() {
            viewModel.getDeckById(deckId).collect { deck: Deck ->
                // Update UI with deck details
                binding.textDeckName.text = deck.deckName
                binding.textDeckDescription.text = deck.description ?: ""

                // Set up RecyclerView for flashcards
                flashcardAdapter =
                    FlashcardAdapter(deck.flashcards) // Pass flashcards from deck
                binding.recyclerViewFlashcards.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerViewFlashcards.adapter = flashcardAdapter
                flashcardAdapter.notifyDataSetChanged()
                // Handle button clicks
                binding.btnStudy.setOnClickListener {
                    //findNavController().navigate(DeckDetailFragmentDirections.actionDeckDetailFragmentToFlashcardStudyFragment(deckId))
                }

                binding.btnAdd.setOnClickListener {
                    showAddFlashcardDialog()
                }

                // Additional button click handlers for add/edit/delete flashcards, delete deck, etc.
            }
        }
    }

    private fun showAddFlashcardDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_flashcard, null)
        val flashcardQuestion = dialogView.findViewById<EditText>(R.id.flashcardQuestion)
        val flashcardAnswer = dialogView.findViewById<EditText>(R.id.flashcardAnswer)

        val builder = AlertDialog.Builder(context)
            .setTitle("Add Deck")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val currentQuestion = flashcardQuestion.text.toString().trim()
                val currentAnswer = flashcardAnswer.text.toString().trim()
                val deckName = binding.textDeckName.text.toString()
                val deckDescription = binding.textDeckDescription.text.toString()
                // Validate input (optional)
                if (currentQuestion.isNotEmpty()) {
                    // Save flashcard to database

                    lifecycleScope.launch {
                        viewModel2.insert(
                            Flashcard(
                                question = currentQuestion,
                                answer = currentAnswer,
                                deckName = deckName
                            )
                        )
                        // Update UI after insertion
                        viewModel.update(
                            Deck(
                                id = deckId,
                                deckName = deckName,
                                description = deckDescription,
                                flashcards = viewModel2.getFlashcardsByDeck(deckName).firstOrNull()
                                    ?: emptyList()

                            )
                        )
                    }

                    // Update UI to reflect the new deck
                    flashcardAdapter.notifyDataSetChanged()

                } else {
                    // Show error message if name is empty
                }
            }
            .setNegativeButton("Cancel", null)

        builder.show()
    }

}
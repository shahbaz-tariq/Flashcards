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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.data.model.Deck
import com.example.flashcards.data.model.Flashcard
import com.example.flashcards.databinding.FragmentDeckDetailBinding
import com.example.flashcards.ui.DeckViewModel
import com.example.flashcards.ui.FlashcardViewModel
import com.example.flashcards.ui.HomeFragmentDirections
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DeckDetailFragment : Fragment(), FlashcardAdapter.FlashcardAdapterListener {

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
                if (deck != null) {
                    binding.textDeckName.text = deck?.deckName ?: ""
                    binding.textDeckDescription.text = deck?.description ?: ""

                    // Set up RecyclerView for flashcards
                    flashcardAdapter =
                        FlashcardAdapter(
                            deck.flashcards,
                            this@DeckDetailFragment
                        ) // Pass flashcards from deck
                    binding.recyclerViewFlashcards.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.recyclerViewFlashcards.adapter = flashcardAdapter
                    flashcardAdapter.notifyDataSetChanged()
                    // Handle button clicks
                    binding.btnStudy.setOnClickListener {
                        //findNavController().navigate(DeckDetailFragmentDirections.actionDeckDetailFragmentToFlashcardStudyFragment(deckId))
                    }

                    binding.btnAdd.setOnClickListener {
                        showAddFlashcardDialog()
                    }

                    binding.btnDelete.setOnClickListener {
                        showDeleteDeckConfirmationDialog(deck)
                    }

                    binding.btnStudy.setOnClickListener {
                        val action = DeckDetailFragmentDirections.actionDeckDetailFragmentToStudyFragment(deck.deckName)
                        findNavController().navigate(action)
                    }

                }
            }
        }
    }

    private fun showAddFlashcardDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_flashcard, null)
        val flashcardQuestion = dialogView.findViewById<EditText>(R.id.flashcardQuestion)
        val flashcardAnswer = dialogView.findViewById<EditText>(R.id.flashcardAnswer)

        val builder = AlertDialog.Builder(context)
            .setTitle("Add Flashcard")
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

    override fun onEditClick(flashcard: Flashcard) {
        // Handle edit click for the flashcard
        // You can open a dialog or navigate to another screen for editing
        showEditFlashcardDialog(flashcard)
    }

    override fun onDeleteClick(flashcard: Flashcard) {
        // Handle delete click for the flashcard
        // You can show a confirmation dialog and proceed with deletion
        showDeleteConfirmationDialog(flashcard)
    }

    private fun showEditFlashcardDialog(flashcard: Flashcard) {
        // Inflate dialog layout with EditTexts for question/answer
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_flashcard, null)
        val editQuestion = dialogView.findViewById<EditText>(R.id.editQuestion)
        val editAnswer = dialogView.findViewById<EditText>(R.id.editAnswer)

        // Pre-fill EditTexts with existing values
        editQuestion.setText(flashcard.question)
        editAnswer.setText(flashcard.answer)

        // Set up dialog and handle "Update" button click
        val builder = AlertDialog.Builder(context)
            .setTitle("Edit Flashcard")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedQuestion = editQuestion.text.toString().trim()
                val updatedAnswer = editAnswer.text.toString().trim()

                val updatedFlashcard = Flashcard(
                    id = flashcard.id,
                    question = updatedQuestion,
                    answer = updatedAnswer,
                    deckName = flashcard.deckName  // Maintain the deck association
                )

                // Call ViewModel to update flashcard in database
                lifecycleScope.launch {
                    viewModel2.update(updatedFlashcard)
                    flashcardAdapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(flashcard: Flashcard) {
        AlertDialog.Builder(context)
            .setTitle("Delete Flashcard")
            .setMessage("Are you sure you want to delete this flashcard?")
            .setPositiveButton("Delete") { _, _ ->
                // Call ViewModel to delete flashcard from database
                lifecycleScope.launch {
                    viewModel2.delete(flashcard)
                    flashcardAdapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDeckConfirmationDialog(deck: Deck) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Deck")
            .setMessage("Are you sure you want to delete this deck?")
            .setPositiveButton("Delete") { _, _ ->
                deleteDeck(deck)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteDeck(deck: Deck) {
        lifecycleScope.launch {
            viewModel.delete(deck)

            val action = DeckDetailFragmentDirections.actionDeckDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

}
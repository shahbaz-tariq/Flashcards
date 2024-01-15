package com.example.flashcards

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.flashcards.data.model.Flashcard
import com.example.flashcards.databinding.FragmentStudyBinding
import com.example.flashcards.ui.FlashcardViewModel
import kotlinx.coroutines.launch

class StudyFragment : Fragment() {

    private lateinit var binding: FragmentStudyBinding
    private lateinit var viewModel: FlashcardViewModel
    private var flashcards: List<Flashcard> = emptyList()
    private var currentFlashcardIndex = 0
    private var showingQuestion = true
    private var deckName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            // Retrieve deckId from navigation arguments
            deckName = it.getString("deckName") // Handle potential null value
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FlashcardViewModel::class.java)

        score = 0


        // Bind the score TextView

        lifecycleScope.launch {
            viewModel.getFlashcardsByDeck(deckName).collect { flashcardList ->
                flashcards = flashcardList
                Log.d("StudyFragment", "Flashcard list size: ${flashcards.size}")
                showFlashcard()
            }
        }

        binding.textScore.text = "Score: $score"

        binding.btnToggle.setOnClickListener {
            toggleCardSide()
        }

        binding.btnLearned.setOnClickListener {
            onLearnedButtonClick()
        }

        binding.btnStillLearning.setOnClickListener {
            onNextButtonClick()
        }

        binding.btnExitStudy.setOnClickListener {
            val action = StudyFragmentDirections.actionStudyFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun showFlashcard() {
        if (currentFlashcardIndex < flashcards.size) {
            val currentFlashcard = flashcards[currentFlashcardIndex]
            val textToShow =
                if (showingQuestion) currentFlashcard.question else currentFlashcard.answer
            binding.textFlashcard.text = textToShow
        }
    }

    private fun toggleCardSide() {
        // Toggle between question and answer
        showingQuestion = !showingQuestion
        showFlashcard()
    }

    private var score = 0

    private fun onLearnedButtonClick() {
        // Increment score when the "Learned" button is clicked
        score++

        binding.textScore.text = "Score: $score"
        // Move to the next flashcard
        onNextButtonClick()
    }

    private fun onNextButtonClick() {
        // Move to the next flashcard
        currentFlashcardIndex++
        // Reset showing side to question
        showingQuestion = true
        // Show the next flashcard
        showFlashcard()
    }
}
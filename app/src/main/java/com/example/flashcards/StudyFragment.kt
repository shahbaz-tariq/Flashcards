package com.example.flashcards

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.flashcards.data.model.Flashcard
import com.example.flashcards.databinding.FragmentStudyBinding
import com.example.flashcards.ui.FlashcardViewModel
import kotlinx.coroutines.launch

class StudyFragment : Fragment() {

    private val viewModel: FlashcardViewModel by viewModels()
    private lateinit var binding: FragmentStudyBinding
    private var flashcards: List<Flashcard> = emptyList()
    private var currentFlashcardIndex = 0
    private var showingQuestion = true
    private var deckName: String? = ""
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            deckName = it.getString("deckName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textScore.text = "Score: $score"

        lifecycleScope.launch {
            viewModel.getFlashcardsByDeck(deckName).collect { flashcardList ->
                flashcards = flashcardList
                showFlashcard()
            }
        }

        binding.btnToggle.setOnClickListener { toggleCardSide() }
        binding.btnLearned.setOnClickListener { onLearnedButtonClick() }
        binding.btnStillLearning.setOnClickListener { onNextButtonClick() }
        binding.btnExitStudy.setOnClickListener {
            findNavController().navigate(StudyFragmentDirections.actionStudyFragmentToHomeFragment())
        }
    }

    private fun showFlashcard() {
        if (currentFlashcardIndex < flashcards.size) {
            val currentFlashcard = flashcards[currentFlashcardIndex]
            val textToShow = if (showingQuestion) currentFlashcard.question else currentFlashcard.answer
            binding.textFlashcard.text = textToShow
        }
    }

    private fun toggleCardSide() {
        showingQuestion = !showingQuestion
        showFlashcard()
    }

    private fun onLearnedButtonClick() {
        if (currentFlashcardIndex < flashcards.size) {
            score++
            binding.textScore.text = "Score: $score"
            onNextButtonClick() // Move to the next flashcard
        }
    }

    private fun onNextButtonClick() {
        currentFlashcardIndex++

        if (currentFlashcardIndex >= flashcards.size) {  // Change to >=
            // Last card reached
            binding.btnLearned.isEnabled = false
            binding.btnStillLearning.isEnabled = false
            binding.btnToggle.isEnabled = false
            binding.textScore.text = "Final Score: $score out of ${flashcards.size}"
            binding.textFlashcard.text = "Congratulations on your learning journey!"
        } else {
            showingQuestion = true // Reset for regular navigation
        }

        showFlashcard()
    }
}

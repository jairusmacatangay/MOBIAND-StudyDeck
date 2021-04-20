package com.example.mobiand_studydeck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mobiand_studydeck.databinding.ActivityWordJumbleBinding
import com.example.mobiand_studydeck.viewModel.WordJumbleViewModel
import com.google.android.material.snackbar.Snackbar

class WordJumbleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordJumbleBinding
    private var totalJumbledWords: Int = 0
    private var enteredWord: String = ""
    private var firstLetter: Char = 'a'
    private var lastLetter: Char = 'a'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWordJumbleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<WordJumbleViewModel>()

        viewModel.totalJumbledWords.observe(this) {
            totalJumbledWords = it
        }

        viewModel.firstLetterOfJumbledWord.observe(this) {
            firstLetter = it
        }

        viewModel.lastLetterOfJumbledWord.observe(this) {
            lastLetter = it
        }

        viewModel.jumbledWordCurrentIndex.observe(this){
            binding.txtCount.text = "$it / $totalJumbledWords"
        }

        viewModel.jumbledword.observe(this){
            binding.txtWord.text = it
        }

        binding.btnCheck.setOnClickListener{
            enteredWord = binding.txtAnswer.text.toString()
            viewModel.checkIfMatch(enteredWord.toUpperCase())

            viewModel.snackbarEvent.observe(this){
                if (it == true){
                    val snack = Snackbar.make(binding.root, "You got it!", Snackbar.LENGTH_SHORT)
                    snack.show()
                    binding.txtAnswer.text.clear()
                } else {
                    val snack = Snackbar.make(binding.root, "Try again!", Snackbar.LENGTH_SHORT)
                    snack.show()
                }
            }
        }

        binding.btnNext.setOnClickListener{
            viewModel.nextWord()
            binding.txtAnswer.text.clear()
        }

        binding.btnBack.setOnClickListener{
            viewModel.previousWord()
            binding.txtAnswer.text.clear()
        }

        binding.btnHint.setOnClickListener{
            val snack = Snackbar.make(binding.root, "First letter is $firstLetter, and last letter is $lastLetter.", Snackbar.LENGTH_SHORT)
            snack.show()
        }
    }
}
package com.example.mobiand_studydeck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobiand_studydeck.databinding.ActivityWordJumbleBinding
import com.example.mobiand_studydeck.flashcards.DatabaseHandler
import com.example.mobiand_studydeck.flashcards.FlashcardsModelClass
import com.google.android.material.snackbar.Snackbar

class WordJumbleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordJumbleBinding
    private lateinit var arrayWordJumble: Array<String?>
    private lateinit var jumbledword: String
    private lateinit var correctWord: String
    private var totalJumbledWords: Int = 0
    private var enteredWord: String = ""
    private var jumbledWordCurrentIndex: Int = 1
    private var snackbarEvent: Boolean = false
    private var firstLetterOfJumbledWord: Char = 'a'
    private var lastLetterOfJumbledWord: Char = 'z'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWordJumbleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayWordJumble = getTermsArray()
        totalJumbledWords = savedInstanceState?.getInt(TOTAL_JUMBLED_WORDS.toString())
                ?: arrayWordJumble.size
        firstLetterOfJumbledWord = savedInstanceState?.getChar(FIRST_LETTER.toString()) ?: 'a'
        lastLetterOfJumbledWord = savedInstanceState?.getChar(LAST_LETTER.toString()) ?: 'b'
        jumbledWordCurrentIndex = savedInstanceState?.getInt(JUMBLED_WORD_INDEX.toString()) ?: 1
        jumbledword = (savedInstanceState?.getString(JUMBLED_WORD)
                ?: arrayWordJumble[0]?.toUpperCase()) as String
        correctWord = (savedInstanceState?.get(CORRECT_WORD)
                ?: arrayWordJumble[0]?.toUpperCase()) as String

        shuffleWord()
        displayValues()

        binding.btnCheck.setOnClickListener {
            enteredWord = binding.txtAnswer.text.toString()
            checkIfMatch(enteredWord.toUpperCase())

            if (snackbarEvent) {
                Snackbar.make(binding.root, "You got it!", Snackbar.LENGTH_SHORT).show()
                binding.txtAnswer.text.clear()
            } else {
                Snackbar.make(binding.root, "Try again!", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener{
            nextWord()
            binding.txtAnswer.text.clear()
        }

        binding.btnBack.setOnClickListener{
            previousWord()
            binding.txtAnswer.text.clear()
        }

        binding.btnHint.setOnClickListener{
            Snackbar.make(binding.root, "First letter is $firstLetterOfJumbledWord, and " +
                    "last letter is $lastLetterOfJumbledWord.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getFlashcards(): ArrayList<FlashcardsModelClass> {
        val dbHandler: DatabaseHandler = DatabaseHandler(this)
        return dbHandler.viewFlashcard()
    }

    private fun append(arr: Array<String?>, element: String): Array<String?> {
        val array = arr.copyOf(arr.size + 1)
        array[arr.size] = element
        return array
    }

    private fun getTermsArray(): Array<String?> {
        var flashcardsList: ArrayList<FlashcardsModelClass> = getFlashcards()
        var arrayFlashcards = emptyArray<String?>()

        for (item in flashcardsList) {
            var term = item.term
            arrayFlashcards = append(arrayFlashcards, term)
        }

        return arrayFlashcards
    }

    private fun checkIfMatch(answer: String){
        var correctAnswer: String = arrayWordJumble[jumbledWordCurrentIndex - 1]?.toUpperCase()
                ?: "Default"
        snackbarEvent = correctAnswer == answer
    }

    private fun shuffleWord(){
        val usedWord = correctWord.toCharArray()
        firstLetterOfJumbledWord = usedWord[0]
        lastLetterOfJumbledWord = usedWord[usedWord.size - 1]
        val shuffledWord = StringBuilder()
        usedWord.shuffle()

        for (letter in usedWord) {
            shuffledWord.append(letter)
        }

        jumbledword = shuffledWord.toString()
    }

    private fun nextWord(){
        if (jumbledWordCurrentIndex < totalJumbledWords){
            jumbledWordCurrentIndex += 1
            loadJumbledWord()
        }
        displayValues()
    }

    private fun previousWord(){
        if (jumbledWordCurrentIndex > 1){
            jumbledWordCurrentIndex -= 1
            loadJumbledWord()
        }
        displayValues()
    }

    private fun displayValues() {
        binding.txtCount.text = "$jumbledWordCurrentIndex / $totalJumbledWords"
        binding.txtWord.text = jumbledword
    }

    private fun loadJumbledWord(){
        correctWord = arrayWordJumble[jumbledWordCurrentIndex - 1]?.toUpperCase() ?: "Default"
        shuffleWord()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(TOTAL_JUMBLED_WORDS.toString(), totalJumbledWords)
        outState.putChar(FIRST_LETTER.toString(), firstLetterOfJumbledWord)
        outState.putChar(LAST_LETTER.toString(), lastLetterOfJumbledWord)
        outState.putInt(JUMBLED_WORD_INDEX.toString(), jumbledWordCurrentIndex)
        outState.putString(JUMBLED_WORD, jumbledword)
        super.onSaveInstanceState(outState)
    }
}
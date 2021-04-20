package com.example.mobiand_studydeck.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobiand_studydeck.databinding.ActivityWordJumbleBinding
import java.util.*

class WordJumbleViewModel: ViewModel() {
    private lateinit var binding: ActivityWordJumbleBinding

    private lateinit var random: Random
    private val arrayWordJumble = arrayOf(
        arrayOf("Biotechnology"),
        arrayOf("Abiotic"),
        arrayOf("Mitosis"),
        arrayOf("Recycling"),
        arrayOf("Hypothesis"),
        arrayOf("Goh"),
        arrayOf("Science"))

    private val _jumbledWordCurrentIndex = MutableLiveData<Int>()
    private val _totalJumbledWords = MutableLiveData<Int>()
    private val _jumbledWord = MutableLiveData<String>()
    private val _correctWord = MutableLiveData<String>()
    private val _snackbarEvent = MutableLiveData<Boolean>()
    private val _firstLetterOfJumbledWord = MutableLiveData<Char>()
    private val _lastLetterOfJumbledWord = MutableLiveData<Char>()

    val jumbledWordCurrentIndex: LiveData<Int> = _jumbledWordCurrentIndex
    val totalJumbledWords: LiveData<Int> = _totalJumbledWords
    val jumbledword: LiveData<String> = _jumbledWord
    val snackbarEvent: LiveData<Boolean> = _snackbarEvent
    val firstLetterOfJumbledWord: LiveData<Char> = _firstLetterOfJumbledWord
    val lastLetterOfJumbledWord: LiveData<Char> = _lastLetterOfJumbledWord


    init {
        random = Random()
        _jumbledWordCurrentIndex.value = 1
        _totalJumbledWords.value = arrayWordJumble.size
        _jumbledWord.value = arrayWordJumble[0][0].toUpperCase()
        _correctWord.value = arrayWordJumble[0][0].toUpperCase()
        _firstLetterOfJumbledWord.value = 'a'
        _lastLetterOfJumbledWord.value = 'z'
        _snackbarEvent.value = false
        shuffleWord()
    }

    fun checkIfMatch(answer: String){
        if (arrayWordJumble[_jumbledWordCurrentIndex.value!! - 1][0].toUpperCase() == answer){
            _snackbarEvent.value = true
        } else{
            _snackbarEvent.value = false
        }
        println("entered answer: $answer")
    }

    fun shuffleWord(){
        var count: Int = 0
        val usedWord = _correctWord.value!!.toCharArray()
        var tempWord = _correctWord.value!!.toCharArray() //to recognize 1st and last letter of the word
        var shuffledWord = StringBuilder()

        usedWord.shuffle()

        for (letter in usedWord) {
            shuffledWord.append(letter)
        }

        _jumbledWord.value = shuffledWord.toString()
        _firstLetterOfJumbledWord.value = usedWord[0]
        _lastLetterOfJumbledWord.value = usedWord[usedWord.size - 1]
    }

    fun nextWord(){
        if (_jumbledWordCurrentIndex.value!! < _totalJumbledWords.value!!){
            _jumbledWordCurrentIndex.value = _jumbledWordCurrentIndex.value!! + 1
            loadJumbledWord()
        }
    }

    fun previousWord(){
        if (_jumbledWordCurrentIndex.value!! > 1){
            _jumbledWordCurrentIndex.value = _jumbledWordCurrentIndex.value!! - 1
            loadJumbledWord()
        }
    }

    fun loadJumbledWord(){
        _correctWord.value =
            arrayWordJumble[_jumbledWordCurrentIndex.value!!.minus(1)][0].toUpperCase()
        shuffleWord()
    }
}
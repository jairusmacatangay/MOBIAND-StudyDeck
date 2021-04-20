package com.example.mobiand_studydeck.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {
    private val arrayQuizContent = arrayOf(
        arrayOf("The ways that humans apply biological concepts to produce products and provide" +
                " services.", "Biotechnology"),
        arrayOf("A nonliving factor or element.", "Abiotic"),
        arrayOf("The sequential differentiation and segregation of replicated chromosomes in a " +
                "cell’s nucleus that precedes complete cell division.", "Mitosis"),
        arrayOf("Collecting and reprocessing a resource or product to make into new products.",
                "Recycling"),
        arrayOf("An assertion subject to verification or proof as a premise from which a " +
                "conclusion is drawn.", "Hypothesis"),
        arrayOf("The last name of our MOBIAND teacher.", "Goh"),
        arrayOf("Search for understanding the natural world using inquiry and experimentation.",
                "Science"),
        arrayOf("The study of earthquakes.", "Seismology"))

    private val _questionCurrentIndex = MutableLiveData<Int>()
    private val _totalQuizItems = MutableLiveData<Int>()
    private val _questionNumber = MutableLiveData<Int>()
    private val _questionContent = MutableLiveData<String>()
    private val _quizScore = MutableLiveData<Int>()

    val questionCurrentIndex: LiveData<Int> = _questionCurrentIndex
    val totalQuizItems: LiveData<Int> = _totalQuizItems
    val questionNumber: LiveData<Int> = _questionNumber
    val questionContent: LiveData<String> = _questionContent
    val quizScore: LiveData<Int> = _quizScore

    init {
        _questionCurrentIndex.value = 1
        _totalQuizItems.value = arrayQuizContent.size
        _questionNumber.value = 1
        _questionContent.value = arrayQuizContent[0][0]
        _quizScore.value = 0
    }

    fun checkAnswer(answer: String){
        if (arrayQuizContent[_questionCurrentIndex.value!! - 1][1] == answer){
            _quizScore.value = _quizScore.value!! + 1
        } else{ }
        println("entered answer: $answer")
        nextQuestion()
    }

    fun nextQuestion(){
        if (_questionCurrentIndex.value!! < _totalQuizItems.value!!){
            _questionNumber.value = _questionNumber.value!! + 1
            _questionCurrentIndex.value = _questionCurrentIndex.value!! + 1
            loadQuestion()
        }
    }

    fun loadQuestion(){
        _questionContent.value = arrayQuizContent[_questionCurrentIndex.value!!.minus(1)][0]
    }
}
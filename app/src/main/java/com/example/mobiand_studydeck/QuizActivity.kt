package com.example.mobiand_studydeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobiand_studydeck.databinding.ActivityQuizBinding
import com.example.mobiand_studydeck.flashcards.DatabaseHandler
import com.example.mobiand_studydeck.flashcards.FlashcardsModelClass

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var arrayQuizContent: Array<Array<String>?>
    private lateinit var questionContent: String
    private var totalQuizItems: Int = 0
    private var quizScore: Int = 0
    private var answer: String = ""
    private var itemCount: Int = 0
    private var questionCurrentIndex: Int = 1
    private var questionNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayQuizContent = getFlashcardsArray()
        questionContent = (savedInstanceState?.getString(QUESTION_CONTENT)
                ?: arrayQuizContent[0]?.get(1)) as String
        questionNumber = savedInstanceState?.getInt(QUESTION_NUMBER.toString()) ?: 1
        itemCount = savedInstanceState?.getInt(ITEM_COUNT.toString()) ?: arrayQuizContent.size
        questionCurrentIndex = savedInstanceState?.getInt(QUESTION_INDEX.toString()) ?: 1
        quizScore = savedInstanceState?.getInt(QUIZ_SCORE.toString()) ?: 0

        totalQuizItems = arrayQuizContent.size

        binding.txtQuesNum.text = "Question $questionNumber"
        binding.txtCount.text = "$questionNumber / $totalQuizItems"
        binding.txtQues.text = questionContent

        binding.btnNext.setOnClickListener{
            answer = binding.txtAnswer.text.toString()
            checkAnswer(answer)
            binding.txtAnswer.text.clear()
            itemCount--
            showResult()
        }
    }

    private fun showResult() {
        if (itemCount == 0) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(QUIZ_SCORE.toString(), quizScore)
            intent.putExtra(TOTAL_ITEMS_OF_QUIZ, totalQuizItems.toString())
            startActivity(intent)
        }
    }

    private fun getFlashcards(): ArrayList<FlashcardsModelClass> {
        val dbHandler: DatabaseHandler = DatabaseHandler(this)
        return dbHandler.viewFlashcard()
    }

    private fun append(arr: Array<Array<String>?>, element: Array<String>): Array<Array<String>?> {
        val array = arr.copyOf(arr.size + 1)
        array[arr.size] = element
        return array
    }

    private fun getFlashcardsArray(): Array<Array<String>?> {
        var flashcardsList: ArrayList<FlashcardsModelClass> = getFlashcards()
        var arrayFlashcards = emptyArray<Array<String>?>()

        for (item in flashcardsList) {
            var term = item.term
            var definition = item.definition
            arrayFlashcards = append(arrayFlashcards, arrayOf(term, definition))
        }

        return arrayFlashcards
    }

    private fun checkAnswer(answer: String) {
        val correctAnswer: String = arrayQuizContent[questionCurrentIndex - 1]?.get(0) ?: "Default"
        if (correctAnswer == answer)
            quizScore += 1
        nextQuestion()
    }

    private fun nextQuestion() {
        if (questionCurrentIndex < totalQuizItems) {
            questionNumber += 1
            questionCurrentIndex += 1
            loadQuestion()
        }
        binding.txtQuesNum.text = "Question $questionNumber"
        binding.txtCount.text = "$questionNumber / $totalQuizItems"
    }

    private fun loadQuestion(){
        questionContent = arrayQuizContent[questionCurrentIndex - 1]?.get(1) ?: "Default"
        binding.txtQues.text = questionContent
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(QUIZ_SCORE.toString(), quizScore)
        outState.putInt(ITEM_COUNT.toString(), itemCount)
        outState.putInt(QUESTION_INDEX.toString(), questionCurrentIndex)
        outState.putString(QUESTION_CONTENT, questionContent)
        super.onSaveInstanceState(outState)
    }
}


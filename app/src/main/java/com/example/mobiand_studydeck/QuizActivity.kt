package com.example.mobiand_studydeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mobiand_studydeck.databinding.ActivityQuizBinding
import com.example.mobiand_studydeck.viewModel.QuizViewModel

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private var totalQuizItems: Int = 0
    private var quizScore: Int = 0
    private var answer: String = ""
    private var itemCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<QuizViewModel>()

        viewModel.totalQuizItems.observe(this){
            totalQuizItems = it
            itemCount = it
        }

        viewModel.questionNumber.observe(this){
            binding.txtQuesNum.text = "Question $it"
            binding.txtCount.text = "$it / $totalQuizItems"
        }

        viewModel.questionContent.observe(this){
            binding.txtQues.text = it
        }

        viewModel.quizScore.observe(this){
            quizScore = it
        }

        binding.btnNext.setOnClickListener{
            answer = binding.txtAnswer.text.toString()
            viewModel.checkAnswer(answer)
            binding.txtAnswer.text.clear()
            itemCount--
            showResult()
        }
    }

    private fun showResult() {
        if (itemCount.equals(0)) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(QUIZ_SCORE, quizScore.toString())
            intent.putExtra(TOTAL_ITEMS_OF_QUIZ, totalQuizItems.toString())
            startActivity(intent)
        }
    }
}


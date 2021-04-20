package com.example.mobiand_studydeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobiand_studydeck.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var totalQuizItems: Int = 0
    private var totalScore: Int = 0
    private var percentageScore: Int = 0
    private lateinit var extra1:String
    private lateinit var extra2:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        extra1 = intent.getStringExtra(QUIZ_SCORE) ?: getString(R.string.totalScore)
        extra2 = intent.getStringExtra(TOTAL_ITEMS_OF_QUIZ) ?: getString(R.string.totalQuestions)
        totalScore = extra1.toInt()
        totalQuizItems = extra2.toInt()

        computePercentage(totalScore)

        binding.txtScore.text = "Score: $totalScore / $totalQuizItems ($percentageScore%)"

        quizFeedback()

        binding.btnQuizRestart.setOnClickListener{
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }

    fun computePercentage(totalScore: Int){
        percentageScore = totalScore * 100 / totalQuizItems
    }

    fun quizFeedback() {
        if (percentageScore == 100){
            binding.txtFeedback.text = "Perfect!"
        } else if (percentageScore <= 99 && percentageScore >= 90){
            binding.txtFeedback.text = "Great Job!"
        } else if (percentageScore <= 89 && percentageScore >= 84){
            binding.txtFeedback.text = "Very Good!!"
        } else if (percentageScore <= 83 && percentageScore >= 80){
            binding.txtFeedback.text = "Good!"
        } else if (percentageScore <= 79 && percentageScore >= 75){
            binding.txtFeedback.text = "Passed!"
        } else if (percentageScore <= 74){
            binding.txtFeedback.text = "Study More!"
        }
    }
}
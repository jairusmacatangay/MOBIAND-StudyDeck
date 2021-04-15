package com.example.mobiand_studydeck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mobiand_studydeck.databinding.ActivityStudyBinding
import com.example.mobiand_studydeck.viewModel.StudyViewModel

class StudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<StudyViewModel>()

        viewModel.flashcardCurrentNum.observe(this) {
            binding.tvFlashcardNumber.text = "$it / ${viewModel.flashcardTotalNum}"
        }

        viewModel.flashcardLabel.observe(this) {
            binding.tvLabel.text = it
        }

        viewModel.flashcardContent.observe(this) {
            binding.tvContent.text = it
        }

        binding.btnFlip.setOnClickListener { viewModel.flipFlashcard() }
        binding.btnNext.setOnClickListener { viewModel.navigateNext() }
        binding.btnBack.setOnClickListener { viewModel.navigateBack() }
    }
}
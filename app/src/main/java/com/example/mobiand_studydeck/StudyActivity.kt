package com.example.mobiand_studydeck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobiand_studydeck.databinding.ActivityStudyBinding
import com.example.mobiand_studydeck.flashcards.DatabaseHandler
import com.example.mobiand_studydeck.flashcards.FlashcardsModelClass

class StudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudyBinding
    private lateinit var flashcardContent: String
    private lateinit var arrayFlashcardsDeck: Array<Array<String>?>
    private var flashcardLabel: String = "Definition"
    private var flashcardFace: String = "Back"
    private var flashcardCurrentNum: Int = 1
    private var flashcardTotalNum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayFlashcardsDeck = getFlashcardsArray()
        flashcardTotalNum = arrayFlashcardsDeck.size
        flashcardCurrentNum = savedInstanceState?.getInt(FLASHCARDNUM.toString()) ?: 1
        flashcardLabel = savedInstanceState?.getString(LABEL) ?: "Definition"
        flashcardContent = (savedInstanceState?.getString(CONTENT) ?:
            arrayFlashcardsDeck[0]?.get(1)) as String
        flashcardFace = savedInstanceState?.getString(FACE) ?: "Back"

        displayValues()

        binding.btnFlip.setOnClickListener { flipFlashcard() }
        binding.btnNext.setOnClickListener { navigateNext() }
        binding.btnBack.setOnClickListener { navigateBack() }
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

    private fun flipFlashcard() {
        if (flashcardLabel == "Definition")
            flashcardLabel = "Term"
        else if (flashcardLabel == "Term")
            flashcardLabel = "Definition"

        if (flashcardFace == "Back") {
            flashcardContent = arrayFlashcardsDeck[flashcardCurrentNum - 1]?.get(0) ?: "Empty"
            flashcardFace = "Front"
        } else if (flashcardFace == "Front") {
            flashcardContent = arrayFlashcardsDeck[flashcardCurrentNum - 1]?.get(1) ?: "Empty"
            flashcardFace = "Back"
        }

        displayValues()
    }

    private fun navigateNext() {
        if (flashcardCurrentNum < flashcardTotalNum) {
            flashcardCurrentNum += 1
            resetFlashcard()
        }
        displayValues()
    }

    private fun navigateBack() {
        if(flashcardCurrentNum > 1) {
            flashcardCurrentNum -= 1
            resetFlashcard()
        }
        displayValues()
    }

    private fun resetFlashcard() {
        flashcardContent = arrayFlashcardsDeck[flashcardCurrentNum - 1]?.get(1) ?: "Empty"
        flashcardFace = "Back"
        flashcardLabel = "Definition"
    }

    private fun displayValues() {
        binding.tvFlashcardNumber.text = "$flashcardCurrentNum / ${arrayFlashcardsDeck.size}"
        binding.tvLabel.text = flashcardLabel
        binding.tvContent.text = flashcardContent
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(FLASHCARDNUM.toString(), flashcardCurrentNum)
        outState.putString(LABEL, flashcardLabel)
        outState.putString(CONTENT, flashcardContent)
        outState.putString(FACE, flashcardFace)
        super.onSaveInstanceState(outState)
    }
}
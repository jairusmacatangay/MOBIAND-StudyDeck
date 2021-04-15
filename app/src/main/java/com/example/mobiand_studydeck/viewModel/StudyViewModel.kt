package com.example.mobiand_studydeck.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudyViewModel: ViewModel() {
    private val _flashcardLabel = MutableLiveData<String>()
    private val _flashcardContent = MutableLiveData<String>()
    private val _flashcardCurrentNum = MutableLiveData<Int>()
    private val _flashcardFace = MutableLiveData<String>()

    val flashcardLabel: LiveData<String> = _flashcardLabel
    val flashcardContent: LiveData<String> = _flashcardContent
    val flashcardCurrentNum: LiveData<Int> = _flashcardCurrentNum
    val flashcardFace: LiveData<String> = _flashcardFace
    var flashcardTotalNum: Int

    var arrayFlashcardsDeck = arrayOf(
        arrayOf("Hydrolysis","the breaking down of polymers (large molecules composed of " +
                "repeating subunits) into monomers (the building blocks ) by adding water"),
        arrayOf("Lipids", "are a structurally diverse group of molecules that are lumped " +
                "together on the basis of their inability to dissolve in water (they are " +
                "non-polar)"),
        arrayOf("Proteins","are a functionally diverse group of molecules with very similar " +
                "primary structures. They consist of amino acids bonded to one another by " +
                "peptide bonds"),
        arrayOf("Enzyme", "a molecule composed at least partially of protein which catalyzes a " +
                "biochemical reaction"),
        arrayOf("Carbohydrates", "essentially hydrated carbon compounds (CH2O)")
    )

    init {
        _flashcardLabel.value = "Definition"
        _flashcardContent.value = arrayFlashcardsDeck[0][1]
        _flashcardCurrentNum.value = 1
        flashcardTotalNum = arrayFlashcardsDeck.size
        _flashcardFace.value = "Back"
    }

    fun flipFlashcard() {
        if (_flashcardLabel.value == "Definition")
            _flashcardLabel.value = "Term"
        else if (_flashcardLabel.value == "Term")
            _flashcardLabel.value = "Definition"

        if (_flashcardFace.value == "Back") {
            _flashcardContent.value =
                arrayFlashcardsDeck[_flashcardCurrentNum.value!!?.minus(1)][0]
            _flashcardFace.value = "Front"
        } else if (_flashcardFace.value == "Front") {
            _flashcardContent.value =
                arrayFlashcardsDeck[_flashcardCurrentNum.value!!?.minus(1)][1]
            _flashcardFace.value = "Back"
        }
    }

    fun navigateNext() {
        if (_flashcardCurrentNum.value!! < flashcardTotalNum) {
            _flashcardCurrentNum.value = _flashcardCurrentNum.value!! + 1
            resetFlashcard()
        }
    }

    fun navigateBack() {
        if(_flashcardCurrentNum.value!! > 1) {
            _flashcardCurrentNum.value = _flashcardCurrentNum.value!! - 1
            resetFlashcard()
        }
    }

    private fun resetFlashcard() {
        _flashcardContent.value =
            arrayFlashcardsDeck[_flashcardCurrentNum.value!!?.minus(1)][1]
        _flashcardFace.value = "Back"
        _flashcardLabel.value = "Definition"
    }
}
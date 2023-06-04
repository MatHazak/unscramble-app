package com.example.android.unscramble.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private val usedWords = mutableListOf<String>()
    private lateinit var currentWord: String


    private fun getNextWord() {
        currentWord = allWordsList.random()
        if (usedWords.contains(currentWord))
            return getNextWord()

        val scrambled = currentWord.toCharArray()
        do {
            scrambled.shuffle()
        } while (String(scrambled) == currentWord)
        _currentScrambledWord.value = String(scrambled)
        _currentWordCount.value = (_currentWordCount.value)?.inc()
        usedWords.add(currentWord)
    }

    fun nextWord(): Boolean {
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else
            false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun checkUserWord(playerWord: String): Boolean {
        return if(playerWord == currentWord) {
            increaseScore()
            true
        } else
            false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        usedWords.clear()
        getNextWord()
    }

    init {
        getNextWord()
    }
}
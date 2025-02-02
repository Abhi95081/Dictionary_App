package com.example.dictionaryapp.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.data.DictionaryApi
import com.example.dictionaryapp.data.Models.WordResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class DictionaryState(
    val searchQuery: String = "",
    val wordResult: WordResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DictionaryViewModel : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DictionaryApi::class.java)

    private val _state = mutableStateOf(DictionaryState())
    val state: State<DictionaryState> = _state

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun searchWord() {
        if (_state.value.searchQuery.isBlank()) {
            _state.value = _state.value.copy(error = "Please enter a word")
            return
        }

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )
                val response = api.getWordMeaning(_state.value.searchQuery)
                val wordResult = response.firstOrNull()
                _state.value = _state.value.copy(
                    isLoading = false,
                    wordResult = wordResult,
                    error = if (wordResult == null) "Word not found" else null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    wordResult = null,
                    error = "An error occurred: ${e.message}"
                )
            }
        }
    }
}

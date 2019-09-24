package com.britomartis.android.britobudget.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.britomartis.android.britobudget.data.MessageRepository

class MainActivityViewModelFactory(
    private val repository: MessageRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainActivityViewModel(repository) as T
}

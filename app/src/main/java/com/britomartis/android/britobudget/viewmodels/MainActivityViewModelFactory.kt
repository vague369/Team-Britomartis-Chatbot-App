package com.britomartis.android.britobudget.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.britomartis.android.britobudget.data.MessageRepository

class MainActivityViewModelFactory(
    val context: Context,
    private val repository: MessageRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainActivityViewModel(context, repository) as T
}

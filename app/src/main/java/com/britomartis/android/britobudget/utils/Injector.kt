package com.britomartis.android.britobudget.utils

import android.content.Context
import com.britomartis.android.britobudget.data.AppDatabase
import com.britomartis.android.britobudget.data.MessageRepository
import com.britomartis.android.britobudget.viewmodels.MainActivityViewModelFactory

object Injector {

    fun getMessageRepository(context: Context): MessageRepository {
        return MessageRepository.getInstance(
            AppDatabase.getInstance(context).messageDao()
        )
    }

    fun provideMainActivityViewModelFactory(context: Context): MainActivityViewModelFactory {
        val repository = getMessageRepository(context)
        return MainActivityViewModelFactory(context, repository)
    }
}
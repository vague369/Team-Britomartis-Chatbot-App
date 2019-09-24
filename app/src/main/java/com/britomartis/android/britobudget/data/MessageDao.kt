package com.britomartis.android.britobudget.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    fun insertMessage(message: Message)

    @Query("SELECT * FROM messages ORDER BY messageTime ASC")
    fun getMessages(): LiveData<List<Message>>
}
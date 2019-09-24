package com.britomartis.android.britobudget

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.britomartis.android.britobudget.ui.ChatAdapter
import com.britomartis.android.britobudget.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatAdapter = ChatAdapter(this, viewModel.messageLiveList.value!!.toList())

        chat_recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        chat_recyclerview.adapter = chatAdapter

        viewModel.messageLiveList.observe(this, Observer {
            Log.d(TAG, "CHANGE OBSERVED")
            chatAdapter.dataset = it.toList()
            chatAdapter.notifyItemInserted(chatAdapter.dataset.size - 1)
        })

        send_button.setOnClickListener {
            val inputText = userinput_edittext.text?.toString()
            Log.d(TAG, inputText)
            viewModel.sendButtonClicked(inputText)
        }
    }

    companion object {
        val TAG = "MainActivity"
    }
}

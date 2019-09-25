package com.britomartis.android.britobudget

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.britomartis.android.britobudget.ui.ChatAdapter
import com.britomartis.android.britobudget.utils.Injector
import com.britomartis.android.britobudget.utils.convertTimeLongToDateString
import com.britomartis.android.britobudget.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels {
        Injector.provideMainActivityViewModelFactory(applicationContext)
    }

    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatAdapter = ChatAdapter(this, listOf())
        chat_recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        chat_recyclerview.adapter = chatAdapter

        // Observe the list of messages
        viewModel.messageLiveList.observe(this, Observer {
            chatAdapter.dataset = it
            chatAdapter.notifyItemInserted(chatAdapter.dataset.size - 1)
            chat_recyclerview.scrollToPosition(chatAdapter.dataset.size - 1)
        })

        send_button.setOnClickListener {
            val inputText = userinput_edittext.text?.toString()
            viewModel.sendButtonClicked(inputText)
            userinput_edittext.setText("")
        }

        // Set the date, depending on the first visible item
        chat_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val p =
                    (chat_recyclerview.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val message = chatAdapter.dataset[p]
                if (DateUtils.isToday(message.messageTime)) {
                    history_date.visibility = View.GONE
                } else {
                    history_date.visibility = View.VISIBLE
                    history_date.text = convertTimeLongToDateString(message.messageTime)
                }
            }
        })
    }

    companion object {
        val TAG = "MainActivity"
    }
}

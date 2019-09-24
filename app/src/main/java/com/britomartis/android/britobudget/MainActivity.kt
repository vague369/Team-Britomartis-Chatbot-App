package com.britomartis.android.britobudget

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.britomartis.android.britobudget.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        send_button.setOnClickListener {
            val inputText = userinput_edittext.text?.toString()
            viewModel.sendButtonClicked(inputText)
        }
    }

}

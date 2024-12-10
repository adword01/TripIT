package com.example.tripit

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView

class customprogressbar(context: Context): Dialog(context) {

    private lateinit var quitDialogText: TextView
    private lateinit var noBtn: TextView
    private lateinit var yesBtn: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_layout)

        quitDialogText = findViewById(R.id.messageProgress)



        window?.setBackgroundDrawable(ColorDrawable(0))
    }

    fun setDialogText(text: String) {
        quitDialogText.text = text
    }

}
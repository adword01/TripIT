package com.example.tripit

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView

class customInfoDialog(context: Context) : Dialog(context) {

    private lateinit var dialogHeading: TextView
    private lateinit var DialogText: TextView
    private lateinit var okayBtn: TextView
    private lateinit var yesBtn: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_popup)

        dialogHeading = findViewById(R.id.DialogHeadingText)
        dialogHeading.visibility=View.GONE
        DialogText = findViewById(R.id.DialogText)
        okayBtn = findViewById(R.id.okayBtn)

        okayBtn.setOnClickListener {
            dismiss()
        }

        window?.setBackgroundDrawable(ColorDrawable(0))
    }

    fun setDialogText(heading : String,text: String) {
        DialogText.text = text
        dialogHeading.text = heading
    }


}
package com.example.tripit

import java.io.Serializable
import java.util.Calendar

class Global : Serializable {

    private var instance: Global? = null
     var isNotificationDialogShown =  false
    var name: String? = null
    var imageUrl: String? = null


    companion object {
        private var instance: Global? = null

        @Synchronized
        fun getInstance(): Global {
            if (instance == null) {
                instance = Global()
            }
            return instance!!
        }
    }
}
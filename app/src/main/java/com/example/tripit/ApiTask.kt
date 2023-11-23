package com.example.tripit

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class ApiTask(private val callback: Callback) : AsyncTask<Void, Void, String>() {

    interface Callback {
        fun onResponse(response: String?)
    }

    override fun doInBackground(vararg params: Void): String? {
        try {
            val url = URL("https://6a01-34-83-239-152.ngrok-free.app/recommend")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val postData = "theme=Religious Sites&rating=3&days=1&latitude=31.59331277&longitude=76.27356938"
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(postData.toByteArray())
            outputStream.flush()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                return response.toString()
            } else {
                Log.e("HTTP_ERROR", "HTTP error code: $responseCode")
            }
        } catch (e: Exception) {
            Log.e("HTTP_ERROR", "Error in HTTP request", e.cause)
        }
        return null
    }

    override fun onPostExecute(result: String?) {
        // Pass the response to the callback interface
        callback.onResponse(result)
    }
}
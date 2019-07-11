package com.theta360.iftttsample.task

import android.util.Log

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*

import java.net.HttpURLConnection.HTTP_OK

class WebhookTask(private val url: String) {

    private val httpClient = OkHttpClient()

    fun request(): Boolean {

        val body = JsonObject().apply {
            addProperty("value1", "hoge")
            addProperty("value2", "fuga")
            addProperty("value3", "piyo")
        }

        val requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=UTF-8"), GSON.toJson(body))
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .addHeader("X-XSRF-Protected", "1")
                .build()

        Log.d(TAG, "START - http request")
        val response = httpClient.newCall(request).execute()
        Log.d(TAG, "FINISH - http request")

        Log.d(TAG, "Status code: ${response.code()}")
        val responseBody = response.body()
        if (responseBody != null)
            Log.d(TAG, responseBody.string())
        return response.code() == HTTP_OK
    }

    companion object {
        private val TAG: String = WebhookTask::class.java.simpleName
        private val GSON = Gson()
    }
}

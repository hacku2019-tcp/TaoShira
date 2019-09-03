package com.tia.my.app.taoshira

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.gms.dynamic.DeferredLifecycleHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*


class RegisterTokenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.button3.setOnClickListener(View.OnClickListener { view ->
            if (!this.editText.text.isBlank()) {
                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->

                    val token = task.result?.token

                    val userId = this.editText.text
                    val pushId = token

                    val json = "{" +
                            "\"apiType\": \"registerToken\"," +
                            "\"userId\": \"${userId}\", " +
                            "\"pushId\": \"${pushId}\""+
                            "}"

                   this.post(json)

                    finish()
                })
            }
        })
    }

    fun post(json: String) = GlobalScope.launch (Dispatchers.Main) {
        val apiUrl = "https://hacku.dragon-egg.org/api"
        val body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json)

        val request = Request.Builder()
            .url(apiUrl)
            .post(body)
            .build()

        val client = OkHttpClient()

        async(Dispatchers.Default) {
            val res = client.newCall(request).execute()
            println(res.body().toString())
        }.await()
    }
}
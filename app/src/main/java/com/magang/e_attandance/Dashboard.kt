package com.magang.e_attandance


import androidx.navigation.ui.setupWithNavController
import com.magang.e_attandance.databinding.ActivityDashboardBinding

import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.magang.e_attandance.api.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val nip = intent.getStringExtra("nip").toString()
        sendRequest(GET, endpoint_h_absen,"nip",nip,binding.historyabsen)
        sendRequest(GET, endpoint_h_pulang,"nip",nip,binding.historypulang)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        AppBarConfiguration(
            setOf(
                R.id.navigation_history, R.id.navigation_absen, R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)
    }

    fun getnip(): String? {
        return intent.getStringExtra("nip")
    }
    fun getnama(): String? {
        return intent.getStringExtra("nama")
    }
    fun getposisi(): String? {
        return intent.getStringExtra("posisi")
    }
    fun getgender(): String? {
        return intent.getStringExtra("gender")
    }
    fun getttl(): String? {
        return intent.getStringExtra("ttl")
    }
    fun getemail(): String? {
        return intent.getStringExtra("email")
    }
    fun getno_hp(): String? {
        return intent.getStringExtra("no_hp")
    }
    fun getalamat(): String? {
        return intent.getStringExtra("alamat")
    }
    fun getabsen(): JSONArray {
        val history: TextView =findViewById(R.id.historyabsen)
        println(history.text.toString())
        val Jarray = JSONArray(history.text.toString())
        var Jobject = JSONObject(Jarray[0].toString())
        return Jobject.getJSONArray("data")
    }
    fun getpulang(): JSONArray {
        val history: TextView = findViewById(R.id.historypulang)
        println(history.text.toString())
        val Jarray = JSONArray(history.text.toString())
        var Jobject = JSONObject(Jarray[0].toString())
        return Jobject.getJSONArray("data")
    }
    private fun sendRequest(
        method: String,
        endpoint: String,
        nip: String?,
        value1: String?,
        history: TextView
    ) {
        val fetchedUrl = getFinalURL(url)
        println("FetchedURL is:$fetchedUrl")
        val fullURL = "$fetchedUrl/$endpoint/$value1"
        val request: Request
        val client: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()
        request =
            Request.Builder()
                .url(fullURL)
                .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    this@Dashboard.runOnUiThread {history.text = "api tidak merespon"}
                }
                override fun onResponse(call: Call, response: Response) {
                    val jsonData: String = response.body!!.string()
                    this@Dashboard.runOnUiThread { history.text = jsonData}
                    response.body?.close()
                }
            })
    }
}
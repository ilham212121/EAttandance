package com.magang.e_attandance.ui.profile

import com.magang.e_attandance.Dashboard
import com.magang.e_attandance.R
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.magang.e_attandance.api.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class EditProfile :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_edit_profile)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val nip = intent.getStringExtra("nip");
        val nama = intent.getStringExtra("nama");
        val posisi = intent.getStringExtra("posisi");
        val gender = intent.getStringExtra("gender");
        val ttl = intent.getStringExtra("ttl");
        val email = intent.getStringExtra("email");
        val no_hp = intent.getStringExtra("no_hp");
        val alamat = intent.getStringExtra("alamat");
        et_profile_email.setText(email)
        et_profile_no_hp.setText(no_hp)
        et_profile_alamat.setText(alamat)
        save_profile.setOnClickListener {
            val old_pswd=et_profile_pswd_old.text.toString()
            val new_pswd=et_profile_pswd_new.text.toString()
            val et_email=et_profile_email.text.toString()
            val et_no_hp=et_profile_no_hp.text.toString()
            val et_alamat=et_profile_alamat.text.toString()
            sendRequest(
                PUT,
                endpointedit,
                "nip",
                "old_pswd",
                "new_pswd",
                "email",
                "no_hp",
                "alamat",
                nip,
                old_pswd,
                new_pswd,
                et_email,
                et_no_hp,
                et_alamat
            )
        }
        cancel_et_profile.setOnClickListener {
            val i = Intent(this, Dashboard::class.java)
            i.putExtra("nip",nip)
            i.putExtra("nama",nama)
            i.putExtra("posisi",posisi)
            i.putExtra("gender",gender)
            i.putExtra("ttl",ttl)
            i.putExtra("email",email)
            i.putExtra("no_hp",no_hp)
            i.putExtra("alamat",alamat)
            startActivity(i)
        }
    }
    private fun sendRequest(
        method: String,
        endpoint: String,
        nip: String?,
        old_pswd: String?,
        new_pswd: String?,
        email: String?,
        no_hp: String?,
        alamat: String?,
        value1:String?,
        value2:String?,
        value3:String?,
        value4:String?,
        value5:String?,
        value6:String?,
    ) {
        val respon: TextView = findViewById<View>(R.id.respon_et_profile) as TextView
        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        val fetchedUrl = getFinalURL(url)
        println("FetchedURL is:$fetchedUrl")
        val fullURL = "$fetchedUrl/$endpoint"
        val request: Request

        val client: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        request =
            if (method == PUT) {
                val formBody: RequestBody = FormBody.Builder()
                    .add(nip!!, value1.toString())
                    .add(old_pswd!!, value2.toString())
                    .add(new_pswd!!, value3.toString())
                    .add(email!!, value4.toString())
                    .add(no_hp!!, value5.toString())
                    .add(alamat!!, value6.toString())
                    .build()
                Request.Builder()
                    .url(fullURL)
                    .put(formBody)
                    .build()
            } else {
                //If it's our get request, it doen't require parameters, hence just sending with the url/
                Request.Builder()
                    .url(fullURL)
                    .build()
            }
        /* this is how the callback get handled */
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    this@EditProfile.runOnUiThread { respon.text = "api tidak merespon" }
                }
                override fun onResponse(call: Call, response: Response) {

                    // Read data on the worker thread
                    val jsonData: String = response.body!!.string()
                    val Jobject = JSONObject(jsonData)
                    // Run view-related code back on the main thread.
                    // Here we display the response message in our text view
                    if (Jobject["msg"].toString()== "data berhasil diupdate") {
                        this@EditProfile.runOnUiThread { respon.text = "" }
                        val data = Jobject.getJSONArray("data")
                        val datalogin = JSONObject(data[0].toString())
                        println(datalogin)
                        val intent = Intent(this@EditProfile , SaveSukses::class.java)
                        intent.putExtra("nip",datalogin["nip"].toString())
                        intent.putExtra("nama",datalogin["nama"].toString())
                        intent.putExtra("posisi",datalogin["posisi"].toString())
                        intent.putExtra("gender",datalogin["gender"].toString())
                        intent.putExtra("ttl",datalogin["ttl"].toString())
                        intent.putExtra("email",datalogin["email"].toString())
                        intent.putExtra("no_hp",datalogin["no_hp"].toString())
                        intent.putExtra("alamat",datalogin["alamat"].toString())
                        startActivity(intent)
                        finish()
                    } else {
                        this@EditProfile.runOnUiThread { respon.text =Jobject["msg"].toString()}
                    }
                }
            })
    }
}
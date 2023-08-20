package com.magang.e_attandance


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class LoginSukses: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sukses)
        val nip = intent.getStringExtra("nip");
        val nama = intent.getStringExtra("nama");
        val posisi = intent.getStringExtra("posisi");
        val gender = intent.getStringExtra("gender");
        val ttl = intent.getStringExtra("ttl");
        val email = intent.getStringExtra("email");
        val no_hp = intent.getStringExtra("no_hp");
        val alamat = intent.getStringExtra("alamat");
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("nip",nip)
            intent.putExtra("nama",nama)
            intent.putExtra("posisi",posisi)
            intent.putExtra("gender",gender)
            intent.putExtra("ttl",ttl)
            intent.putExtra("email",email)
            intent.putExtra("no_hp",no_hp)
            intent.putExtra("alamat",alamat)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
package com.magang.e_attandance.ui.absen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View
import com.magang.e_attandance.Dashboard
import com.magang.e_attandance.R
import kotlinx.android.synthetic.main.activity_absen_response.*

class AbsenResponse : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen_response)
        val nip = intent.getStringExtra("nip")
        val nama = intent.getStringExtra("nama")
        val posisi = intent.getStringExtra("posisi")
        val gender = intent.getStringExtra("gender")
        val ttl = intent.getStringExtra("ttl")
        val email = intent.getStringExtra("email")
        val no_hp = intent.getStringExtra("no_hp")
        val alamat = intent.getStringExtra("alamat")
        val tanggal = intent.getStringExtra("tanggal")
        val waktu = intent.getStringExtra("waktu")
        val msg = intent.getStringExtra("msg")
        if (msg=="kamu absen tepat waktu") {
            tanda.setImageResource(R.drawable.berhasil)
            textwaktu.setTextColor(Color.parseColor(""))
            textstatus.text = "Absen Berhasil"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                var html = "<font color='#46A599'>Behasil absen</font> pada tanggal :\n" + tanggal
                texttanggal.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                html = "<font color='#46A599'>"+waktu+"</font>"
                textwaktu.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            } else {
                var html = "<font color='#46A599'>Behasil absen</font> pada tanggal :\n" + tanggal
                texttanggal.text = Html.fromHtml(html)
                html = "<font color='#46A599'>"+waktu+"</font>"
                textwaktu.text = Html.fromHtml(html)
            }
        }
        else if(msg=="kamu terlambat"){
            tanda.setImageResource(R.drawable.gagal)
            textstatus.text = "Absen Berhasil"
            texttanggal.visibility = View.INVISIBLE
            textwaktu.visibility = View.INVISIBLE
            text1.visibility = View.INVISIBLE
            textgagal.visibility = View.VISIBLE
            textgagal.text = msg
            textgagal.setTextColor(Color.parseColor("#E1427B"))
        }
        else{
            tanda.setImageResource(R.drawable.gagal)
            textstatus.text = "Absen Gagal"
            texttanggal.visibility = View.INVISIBLE
            textwaktu.visibility = View.INVISIBLE
            text1.visibility = View.INVISIBLE
            textgagal.visibility = View.VISIBLE
            textgagal.text = msg
            textgagal.setTextColor(Color.parseColor("#E1427B"))
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(
                this , Dashboard::class.java
            )
            i.putExtra("nip", nip)
            i.putExtra("nama", nama)
            i.putExtra("posisi", posisi)
            i.putExtra("gender", gender)
            i.putExtra("ttl", ttl)
            i.putExtra("email", email)
            i.putExtra("no_hp", no_hp)
            i.putExtra("alamat", alamat)
            startActivity(i)
            finish()
        }, 3000L)
    }
}
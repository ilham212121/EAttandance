package com.magang.e_attandance.api

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


fun getFinalURL(url: String?): String? {
    val con = URL(url).openConnection() as HttpURLConnection
    con.instanceFollowRedirects = false
    try {
        con.connect()
        con.inputStream
        if (con.responseCode == HttpURLConnection.HTTP_MOVED_PERM || con.responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            val redirectUrl = con.getHeaderField("Location")
            return getFinalURL(redirectUrl)
        }
        return url
    }catch (e: IOException){
        return "tidak ada respon"
    }
}
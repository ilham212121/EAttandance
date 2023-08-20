package com.magang.e_attandance.ui.absen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AbsenViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Syarat sebelum Absen : "
    }
    val text: LiveData<String> = _text
}
package com.magang.e_attandance.ui.absen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magang.e_attandance.databinding.FragmentAbsenBinding
import com.magang.e_attandance.Dashboard

class AbsenFragment : Fragment() {

    private var _binding: FragmentAbsenBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[AbsenViewModel::class.java]

        _binding = FragmentAbsenBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity: Dashboard? = activity as Dashboard
        val btnAbsen : Button= binding.btnAbsen
        val btnPulang : Button= binding.btnPulang

        val i = Intent(context, Absen::class.java)
        i.putExtra("nip",activity?.getnip())
        i.putExtra("nama",activity?.getnama())
        i.putExtra("posisi",activity?.getposisi())
        i.putExtra("gender",activity?.getgender())
        i.putExtra("ttl",activity?.getttl())
        i.putExtra("email",activity?.getemail())
        i.putExtra("no_hp",activity?.getno_hp())
        i.putExtra("alamat",activity?.getalamat())
        btnAbsen.setOnClickListener{
            i.putExtra("msg",btnAbsen.text.toString())
            this@AbsenFragment.startActivity(i)
        }
        btnPulang.setOnClickListener{
            i.putExtra("msg",btnPulang.text.toString())
            this@AbsenFragment.startActivity(i)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
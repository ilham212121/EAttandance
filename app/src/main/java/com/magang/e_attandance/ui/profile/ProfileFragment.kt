package com.magang.e_attandance.ui.profile

import com.magang.e_attandance.databinding.FragmentProfileBinding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magang.e_attandance.Dashboard
import com.magang.e_attandance.Login
import com.magang.e_attandance.ui.profile.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity: Dashboard? = activity as Dashboard
        binding.textProfile.text = activity?.getnama()
        binding.profileNip.text = activity?.getnip()
        binding.profileNama.text = activity?.getnama()
        binding.profilePosisi.text = activity?.getposisi()
        binding.profileGender.text = activity?.getgender()
        binding.profileTtl.text = activity?.getttl()
        binding.profileEmail.text = activity?.getemail()
        binding.profileNoHp.text = activity?.getno_hp()
        binding.profileAlamat.text = activity?.getalamat()
        binding.profileDeviceId.text = getDeviceId()
        binding.profileEdit.setOnClickListener {
            val intent = Intent(context, EditProfile::class.java)
            intent.putExtra("nip",activity?.getnip())
            intent.putExtra("nama",activity?.getnama())
            intent.putExtra("posisi",activity?.getposisi())
            intent.putExtra("gender",activity?.getgender())
            intent.putExtra("ttl",activity?.getttl())
            intent.putExtra("email",activity?.getemail())
            intent.putExtra("no_hp",activity?.getno_hp())
            intent.putExtra("alamat",activity?.getalamat())
            startActivity(intent)
        }
        binding.profileLogout.setOnClickListener {
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
        }
        return root
    }

    private fun getDeviceId() : String
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            val deviceId: String
            deviceId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID);
            return  deviceId
        } else {
            val telephonyManager = context?.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.imei
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
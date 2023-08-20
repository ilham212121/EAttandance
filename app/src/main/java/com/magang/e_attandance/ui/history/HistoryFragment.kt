package com.magang.e_attandance.ui.history


import com.magang.e_attandance.databinding.FragmentHistoryBinding
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.e_attandance.Dashboard
import com.magang.e_attandance.R
import com.magang.e_attandance.api.*
import kotlinx.android.synthetic.main.fragment_history.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HistoryViewModel::class.java]

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity: Dashboard? = activity as Dashboard?
        val data = activity?.getabsen()
        binding.recycleviewhistory.layoutManager = LinearLayoutManager(context)
        val datahistory = ArrayList<DetailHistory>()
        for (i in 0 until data?.length()!!) {
            if (i <= 13) {
                val history = JSONObject(data[i].toString())
                datahistory.add(
                    DetailHistory(
                        history["tanggal"].toString(),
                        history["waktu"].toString(),
                        history["status"].toString()
                    )
                )
            } else {
                null
            }

        }

        val adapter = HistoryAdapter(datahistory)
        binding.recycleviewhistory.adapter = adapter
        val textView: TextView = binding.textHistory

        binding.btnSearch.setOnClickListener {
            val searchDate = binding.searchDate.text.toString()
            val searchStatus = binding.searchStatus.text.toString()
            if (binding.textHistory.text=="History Absen"){
                val data = activity?.getabsen()
                Toast.makeText(context, "Search data..", Toast.LENGTH_SHORT).show()
                // sendRequest(GET, endpoint_h_pulang,"nip",activity?.getnip(),"tanggal",searchDate,"status",searchStatus,binding.historypulang)
                val datahistory = ArrayList<DetailHistory>()
                for (i in 0 until data?.length()!!) {
                    if (i <= 13) {
                        val history = JSONObject(data[i].toString())
                        if (searchDate == ""){
                            if (history["status"].toString() == searchStatus) {
                                datahistory.add(
                                    DetailHistory(
                                        history["tanggal"].toString(),
                                        history["waktu"].toString(),
                                        history["status"].toString()
                                    )
                                )
                            }

                        }
                        else if (searchStatus == ""){
                            if (history["tanggal"].toString() == searchDate) {
                                datahistory.add(
                                    DetailHistory(
                                        history["tanggal"].toString(),
                                        history["waktu"].toString(),
                                        history["status"].toString()
                                    )
                                )
                            }

                        }
                        else{
                            if (history["tanggal"].toString() == searchDate) {
                                if (history["status"].toString() == searchStatus) {
                                    datahistory.add(
                                        DetailHistory(
                                            history["tanggal"].toString(),
                                            history["waktu"].toString(),
                                            history["status"].toString()
                                        )
                                    )
                                }
                            }
                        }

                    } else {
                        null
                    }

                }

                val adapter = HistoryAdapter(datahistory)
                binding.recycleviewhistory.adapter = adapter
                val textView: TextView = binding.textHistory
            }
            else if (binding.textHistory.text=="History Pulang"){
                val data = activity?.getpulang()
                Toast.makeText(context, "Search data..", Toast.LENGTH_SHORT).show()
                // sendRequest(GET, endpoint_h_pulang,"nip",activity?.getnip(),"tanggal",searchDate,"status",searchStatus,binding.historypulang)
                val datahistory = ArrayList<DetailHistory>()
                for (i in 0 until data?.length()!!) {
                    if (i <= 13) {
                        val history = JSONObject(data[i].toString())
                        if (searchDate == ""){
                            if (history["status"].toString() == searchStatus) {
                                datahistory.add(
                                    DetailHistory(
                                        history["tanggal"].toString(),
                                        history["waktu"].toString(),
                                        history["status"].toString()
                                    )
                                )
                            }

                        }
                        else if (searchStatus == ""){
                            if (history["tanggal"].toString() == searchDate) {
                                datahistory.add(
                                    DetailHistory(
                                        history["tanggal"].toString(),
                                        history["waktu"].toString(),
                                        history["status"].toString()
                                    )
                                )
                            }

                        }
                        else{
                            if (history["tanggal"].toString() == searchDate) {
                                if (history["status"].toString() == searchStatus) {
                                    datahistory.add(
                                        DetailHistory(
                                            history["tanggal"].toString(),
                                            history["waktu"].toString(),
                                            history["status"].toString()
                                        )
                                    )
                                }
                            }
                        }

                    } else {
                        null
                    }

                }

                val adapter = HistoryAdapter(datahistory)
                binding.recycleviewhistory.adapter = adapter
                val textView: TextView = binding.textHistory
            }

        }

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
            binding.historyAbsen.setOnClickListener {

                binding.textHistory.text="History Absen"
                historyAbsen.text="history absen"
                historypulang.background= null
                historypulang.setTextColor(R.color.black)
                historyAbsen.setBackgroundResource(R.drawable.btn_switch)
                val data = activity.getabsen()
                binding.recycleviewhistory.layoutManager = LinearLayoutManager(context)
                val datahistory = ArrayList<DetailHistory>()
                for (i in 0 until data.length()) {
                    if (i <= 13) {
                        val history = JSONObject(data[i].toString())
                        datahistory.add(
                            DetailHistory(
                                history["tanggal"].toString(),
                                history["waktu"].toString(),
                                history["status"].toString()
                            )
                        )
                    } else {
                        null
                    }
                }
                val adapter = HistoryAdapter(datahistory)
                binding.recycleviewhistory.adapter = adapter
            }

        binding.historypulang.setOnClickListener {
                binding.textHistory.text="History Pulang"
                historypulang.text="history pulang"
                historyAbsen.background= null
                historypulang.setTextColor(R.color.black)
                historypulang.setBackgroundResource(R.drawable.btn_switch)
                val data = activity.getpulang()
                binding.recycleviewhistory.layoutManager = LinearLayoutManager(context)
                val datahistory = ArrayList<DetailHistory>()
                for (i in 0 until data.length()) {
                    if (i <= 13) {
                        val history = JSONObject(data[i].toString())
                        datahistory.add(
                            DetailHistory(
                                history["tanggal"].toString(),
                                history["waktu"].toString(),
                                history["status"].toString()
                            )
                        )
                    } else {
                        null
                    }
                }
                val adapter = HistoryAdapter(datahistory)
                binding.recycleviewhistory.adapter = adapter
            }
        return root
    }
    private fun sendRequest(
        method: String,
        endpoint: String,
        nip: String?,
        value1: String?,
        tanggal: String?,
        value2: String?,
        status: String?,
        value3: String?,
        historyTv: TextView
    ) {
        val fetchedUrl = getFinalURL(url)
        println("FetchedURL is:$fetchedUrl")
        val fullURL = "$fetchedUrl/$endpoint/$value1/$value2/$value3"
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
                }
                override fun onResponse(call: Call, response: Response) {
                    val jsonData: String = response.body!!.string()
                    response.body?.close()
                }
            })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
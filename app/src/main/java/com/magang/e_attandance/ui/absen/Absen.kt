package com.magang.e_attandance.ui.absen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.magang.e_attandance.R
import com.magang.e_attandance.api.*
import kotlinx.android.synthetic.main.activity_absen.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Absen : AppCompatActivity() {
    private val cameraRequest = 1888
    lateinit var imageView: ImageView
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    //private lateinit var executor: Executor
    //private lateinit var biometricPrompt: BiometricPrompt
    //private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var photoFile: File
    val REQUEST_TAKE_PHOTO = 0
    lateinit var currentPhotoPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        ActivityCompat.requestPermissions(this,arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_PERMISSION_REQ_CODE)
        imageView = findViewById(R.id.imageView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val nip = intent.getStringExtra("nip").toString()
        button.setOnClickListener {
            dispatchTakePictureIntent()
        }
        getCurrentLocation()
        btn_kirim.setOnClickListener {
            val mimeType = photoFile?.let { getMimeType(it) }
            val msg = intent.getStringExtra("msg")
            if (msg == "Absen") {
                sendRequest(
                    POST,
                    endpointabsen,
                    "nip",
                    "latitude",
                    "longitude",
                    "image",
                    nip,
                    latitude.toString(),
                    longitude.toString(),
                    photoFile.toString(),
                    photoFile.asRequestBody(mimeType?.toMediaTypeOrNull())
                )
            } else if (msg == "Pulang") {
                sendRequest(
                    POST,
                    endpointpulang,
                    "nip",
                    "latitude",
                    "longitude",
                    "image",
                    nip,
                    latitude.toString(),
                    longitude.toString(),
                    photoFile.toString(),
                    photoFile.asRequestBody(mimeType?.toMediaTypeOrNull())
                )
            }
        }
    }
    /**
     * ini kodingan scan sidik jari
    executor = ContextCompat.getMainExecutor(this)
    biometricPrompt = BiometricPrompt(this, executor,
    object : BiometricPrompt.AuthenticationCallback() {
    override fun onAuthenticationError(errorCode: Int,
    errString: CharSequence) {
    super.onAuthenticationError(errorCode, errString)
    if (errString == "Cancel") {
    Toast.makeText(
    applicationContext,
    "Canceled", Toast.LENGTH_SHORT
    )
    .show()
    } else {
    Toast.makeText(
    applicationContext,
    "Authentication error: $errString", Toast.LENGTH_SHORT
    )
    .show()
    }
    }

    override fun onAuthenticationSucceeded(
    result: BiometricPrompt.AuthenticationResult) {
    super.onAuthenticationSucceeded(result)
    Toast.makeText(applicationContext,
    "Anda Berhasil Absen!", Toast.LENGTH_SHORT)
    .show()
    this@CameraActivity.startActivity(Intent(applicationContext, AbsenSukses::class.java))
    this@CameraActivity.finish()
    }

    override fun onAuthenticationFailed() {
    super.onAuthenticationFailed()
    Toast.makeText(applicationContext, "Authentication failed",
    Toast.LENGTH_SHORT)
    .show()
    }
    })

    promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Silahkan Masukan Sidik Jari Anda")
    .setSubtitle("error? Pengaturan > add Fingerprint")
    .setNegativeButtonText("Cancel")
    .build()

    // Prompt appears when user clicks "Log in".
    // Consider integrating with the keystore to unlock cryptographic operations,
    // if needed by your app.
    val biometricLoginButton =
    findViewById<Button>(R.id.btn_finger)
    biometricLoginButton.setOnClickListener {
    biometricPrompt.authenticate(promptInfo)
    }
     */

    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    println("Error occurred while creating the File")
                    return
                }
                // Continue only if the File was successfully created
                photoFile?.also{
                    val photoURI: Uri = getUriForFile(
                        this,
                        "com.rizky.ilham.pe_absen",
                        it
                    )
                    //save picture
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    //use front camera
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmm").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(this, "Kamu Perlu Mengizinkan Akses Lokasi",
                        Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentLocation() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                } else -> {
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            radar.text = "GPS Is Enabled"
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
                return
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                        location ->  // getting the last known or current location
                    if (location.latitude == null && location.latitude == null) {

                    }
                    else{
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                    println(latitude)
                    println(longitude)
                    // btn_kirim.visibility = View.INVISIBLE
                    //wilayah rumah
                    /** if(location.latitude>=-6.9517150){
                        println(">= true")
                    }
                    if(location.latitude<=-6.9511600){
                        println("<= true")
                    }
                    if ((location.latitude<=-6.9511600) && (location.latitude>=-6.9517150)) {
                        if ((location.longitude >= 109.1832580) && (location.longitude <= 109.1836000)) {
                            radar.text ="kamu berada di area Rumah, Silahkan Absen Berangkat / pulang"
                            btn_kirim.visibility = View.VISIBLE
                        } else {
                            radar.text ="kamu berada di luar area . Tolong masuk lebih dalam ke area Rumah"
                        }
                    }

                    else {
                    if (location.accuracy <= 20) {
                        radar.text = "kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                    } else if (location.accuracy <= 40) {
                        radar.text = "kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                    }else if (location.accuracy >40) {
                        radar.text ="kamu berada sangat jauh di luar area. silahkan ke Rumah"
                    } else {
                        radar.text = "kamu berada sangat jauh di luar area. silahkan ke Rumah"
                    }
                    }**/

                     //wilayah poltek
                    /**
                     if ((location.latitude<=-6.868359) && (location.latitude>=-6.868826)) {
                         if ((location.longitude >= 109.106126) && (location.longitude <= 109.108074)) {
                             radar.text ="kamu berada di area Politeknik Harapan Bersama, Silahkan Absen Berangkat / pulang"
                             btn_kirim.visibility = View.VISIBLE
                         } else {
                             radar.text ="kamu berada di luar area . Tolong masuk lebih dalam ke area Politeknik Harapan Bersama"
                         }
                     }

                    else {
                         if (location.accuracy <= 20) {
                             radar.text ="kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                         }else if (location.accuracy <= 40) {
                             radar.text ="kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                         }else if (location.accuracy >40){
                             radar.text = "kamu berada sangat jauh di luar area. silahkan ke area Politeknik Harapan Bersama"
                         }else{
                             radar.text = "kamu berada sangat jauh di luar area. silahkan ke area Politeknik Harapan Bersama"
                         }
                     }
                    if ((location.latitude<= -6.868826)) {
                        if ((location.longitude >= 109.105986) && (location.longitude <= 109.107127)) {
                            radar.text ="kamu berada di area Politeknik Harapan Bersama, Silahkan Absen Berangkat / pulang"
                            btn_kirim.visibility = View.VISIBLE
                        } else {
                            radar.text ="kamu berada di luar area. Tolong masuk lebih dalam ke area Politeknik Harapan Bersama"

                        }
                    }
                    else {
                        if (location.accuracy <= 20) {
                            radar.text ="kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                        }else if (location.accuracy <= 40) {
                            radar.text ="kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                        }else if (location.accuracy >40){
                            radar.text = "kamu berada sangat jauh di luar area. silahkan ke area Politeknik Harapan Bersama"
                        }else{
                            radar.text = "kamu berada sangat jauh di luar area. silahkan ke area Politeknik Harapan Bersama"
                        }
                    }
                    **/

                     // wilayah rsi
                    /**
                      if ((location.latitude <= -6.8751410) && (location.latitude >= -6.8764900)) {
                         // IF  -6.8764900 >= latitude <= -6.8751410
                         // AND IF 109.1279940 >= longitude <= 109.1290940
                         // THEN "kamu berada di area Rumah Sakit Harapan Anda, Silahkan Absen"

                         // IF  -6.8764900 >= latitude <= -6.8751410
                         // AND if ((latitude <= -6.8760800
                         // AND IF 109.1279940 >= longitude <= 109.1293340
                         // THEN "kamu berada di area Rumah Sakit Harapan Anda, Silahkan Absen"

                         // Else "kamu berada di luar area Rumah Sakit Harapan Anda. Tolong masuk lebih dalam ke area Rumah Sakit Harapan Anda"
                        if ((location.longitude >= 109.1279940) && (location.longitude <= 109.1290940)) {
                            radar.text ="kamu berada di area Rumah Sakit Harapan Anda, Silahkan Absen"
                            latitude = location.latitude
                            longitude = location.longitude
                            btn_kirim.visibility = View.VISIBLE
                        } else {
                            radar.text ="kamu berada di luar area Rumah Sakit Harapan Anda. Tolong masuk lebih dalam ke area Rumah Sakit Harapan Anda"
                        }
                        if (location.latitude<=-6.8760800){
                            if ((location.longitude >= 109.1279940) && (location.longitude <= 109.1293340)) {
                                radar.text =
                                    "kamu berada di area Rumah Sakit Harapan Anda, Silahkan Absen"
                                latitude = location.latitude
                                longitude = location.longitude
                                btn_kirim.visibility = View.VISIBLE
                            } else {
                                radar.text ="kamu berada di luar area Rumah Sakit Harapan Anda. Tolong masuk lebih dalam ke area Rumah Sakit Harapan Anda"
                            }
                        }
                    } else {
                        if (location.accuracy <= 20) {
                            radar.text ="kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                        }else if (location.accuracy <= 40) {
                            radar.text ="kamu berada 20 meter di luar area. silahkan masuk lebih dalam"
                        }else if (location.accuracy >40){
                            radar.text = "kamu berada sangat jauh di luar area. silahkan ke area Rumah Sakit Harapan Anda"
                        }else{
                            radar.text = "kamu berada sangat jauh di luar area. silahkan ke area Rumah Sakit Harapan Anda"
                        }
                    }
                    **/
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed on getting current location",
                        Toast.LENGTH_SHORT).show()
                }
        }
        else {
            radar.text = "Tolong nyalakan Gps"
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && data != null){
            val photo = data!!.extras!!["data"] as Bitmap?
            getCurrentLocation()
            this.runOnUiThread {
                imageView.setImageBitmap(photo)
                button.text = "foto ulang"
            }
        }
    }
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
    private fun sendRequest(
        method: String,
        endpoint: String,
        nip: String?,
        latitude: String?,
        longitude: String?,
        image: String?,
        value1: String?,
        value2: String?,
        value3: String?,
        imagename: String,
        imagevalue: RequestBody,
    ) {
        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        val fetchedUrl = getFinalURL(url)
        println("FetchedURL is:$fetchedUrl")
        val fullURL = "$fetchedUrl/$endpoint"
        val mynip =intent.getStringExtra("nip")
        val nama =intent.getStringExtra("nama")
        val posisi = intent.getStringExtra("posisi")
        val gender = intent.getStringExtra("gender")
        val ttl =intent.getStringExtra("ttl")
        val email = intent.getStringExtra("email")
        val no_hp = intent.getStringExtra("no_hp")
        val alamat = intent.getStringExtra("alamat")
        val request: Request
        val client: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()
        /* If it is a post request, then we have to pass the parameters inside the request body*/request =
            if (method == POST) {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(nip!!, value1.toString())
                        .addFormDataPart(image!!,imagename,imagevalue)
                        .addFormDataPart(latitude!!,value2.toString())
                        .addFormDataPart(longitude!!,value3.toString())
                        .build()
                Request.Builder()
                    .url(fullURL)
                    .post(requestBody)
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
                }
                override fun onResponse(call: Call, response: Response) {

                    // Read data on the worker thread
                    val jsonData: String = response.body!!.string()
                    val Jobject = JSONObject(jsonData)
                    // Run view-related code back on the main thread.
                    // Here we display the response message in our text view

                    if (Jobject["msg"].toString()== "kamu absen tepat waktu") {
                        val i = Intent(
                            this@Absen , AbsenResponse::class.java
                        )
                        i.putExtra("msg",Jobject["msg"].toString())
                        i.putExtra("tanggal",Jobject["tanggal"].toString())
                        i.putExtra("waktu",Jobject["waktu"].toString())
                        i.putExtra("nip", mynip)
                        i.putExtra("nama", nama)
                        i.putExtra("posisi", posisi)
                        i.putExtra("gender", gender)
                        i.putExtra("ttl", ttl)
                        i.putExtra("email", email)
                        i.putExtra("no_hp", no_hp)
                        i.putExtra("alamat", alamat)
                        startActivity(i)
                        finish()
                        response.body?.close()
                    }
                    else{
                        val i = Intent(
                            this@Absen, AbsenResponse::class.java
                        )
                        i.putExtra("msg",Jobject["msg"].toString())
                        i.putExtra("nip", mynip)
                        i.putExtra("nama", nama)
                        i.putExtra("posisi", posisi)
                        i.putExtra("gender", gender)
                        i.putExtra("ttl", ttl)
                        i.putExtra("email", email)
                        i.putExtra("no_hp", no_hp)
                        i.putExtra("alamat", alamat)
                        startActivity(i)
                        finish()
                        response.body?.close()
                    }
                }
            })
    }
}
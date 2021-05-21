package com.example.thefoodexplorer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.viewbinding.BuildConfig
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.fragment.SearchImageFragment
import com.example.thefoodexplorer.fragment.SearchTextFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    lateinit var currentPhotoPath: String

    companion object{
        const val REQUEST_PERMISSION = 100
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }

    private fun setupUI() {
        binding.favorite.setOnClickListener { onFavoriteTap() }
        binding.camera.setOnClickListener { onCameraTap() }
        binding.search.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    onSearchFoodByText()
                    true
                }
                else -> false
            }
        }
    }

    private fun onSearchFoodByText() {
//        TODO("Not yet implemented")
        val query = (binding.search.text ?: "").toString()
        if (query.isEmpty()) return
        supportFragmentManager.beginTransaction().apply {
            replace(binding.placeholder.id, SearchTextFragment.newInstance("$query "), "search-text")
//            addToBackStack(null)
            commit()
        }
    }

    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_${timestamp}", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun onCameraTap() {
//        TODO("Not yet implemented")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createCapturedPhoto()
        } catch (ex: IOException) {
            // If there is error while creating the File, it will be null
            null
        }
        photoFile?.also {
            val photoURI = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                it
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun onFavoriteTap() {
//        TODO("Not yet implemented")
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    binding.search.text?.clear()
                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            binding.placeholder.id,
                            SearchImageFragment.newInstance(currentPhotoPath),
                            "search-image"
                        )
//                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }
    }
}
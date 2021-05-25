package com.example.thefoodexplorer.ui.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.ui.base.ViewModelFactory
import com.example.thefoodexplorer.ui.main.view.fragment.SearchImageFragment
import com.example.thefoodexplorer.ui.main.view.fragment.SearchTextFragment
import com.example.thefoodexplorer.ui.main.viewmodel.HomeViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    lateinit var currentPhotoPath: String

    companion object{
        const val REQUEST_PERMISSION = 100
        const val REQUEST_IMAGE_CAPTURE = 1
        const val FRAGMENT_TAG_QUERY = "search-text"
        const val FRAGMENT_TAG_IMAGE = "search-image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this@HomeActivity,
            ViewModelFactory.getInstance()
        )[HomeViewModel::class.java]
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
            replace(binding.placeholder.id, SearchTextFragment.newInstance("$query "), FRAGMENT_TAG_QUERY)
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
                            FRAGMENT_TAG_IMAGE
                        )
                        commit()
                    }
                }
            }
        }
    }
}
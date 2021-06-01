package com.example.thefoodexplorer.ui.main.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.databinding.ContentHomeBinding
import com.example.thefoodexplorer.ui.base.ViewModelFactory
import com.example.thefoodexplorer.ui.main.adapter.FoodListAdapter
import com.example.thefoodexplorer.ui.main.view.fragment.SearchImageFragment
import com.example.thefoodexplorer.ui.main.view.fragment.SearchTextFragment
import com.example.thefoodexplorer.ui.main.viewmodel.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var _binding: ContentHomeBinding
    private lateinit var viewModel: HomeViewModel
    lateinit var currentPhotoPath: String

    companion object {
        const val REQUEST_PERMISSION = 100
        const val REQUEST_IMAGE_CAPTURE = 1
        const val FRAGMENT_TAG_QUERY = "search-text"
        const val FRAGMENT_TAG_IMAGE = "search-image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        _binding = binding.contentHomeFragment
        setContentView(binding.root)

        setupUI()
        setupViewModel()
        showAllFood()
    }

    private fun showAllFood() {
        val foodListAdapter = FoodListAdapter()
        viewModel.getAllFood().observe(this, {food ->
            if (food != null){
                foodListAdapter.setFood(food.data)
                foodListAdapter.notifyDataSetChanged()
            }
        })
        _binding.rvListFood.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = foodListAdapter
        }
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
        binding.options.setOnClickListener(this)
        _binding.camera.setOnClickListener { onCameraTap() }
        _binding.search.setOnEditorActionListener { _, actionId, _ ->
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
        val query = (_binding.search.text ?: "").toString()
        _binding.rvListFood.visibility = View.INVISIBLE
        if (query.isEmpty()) return
        supportFragmentManager.beginTransaction().apply {
            replace(
                _binding.placeholder.id,
                SearchTextFragment.newInstance("$query "),
                FRAGMENT_TAG_QUERY
            )
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
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val photoFile: File? = try {
//            createCapturedPhoto()
//        } catch (ex: IOException) {
//            null
//        }
//        photoFile?.also {
//            val photoURI = FileProvider.getUriForFile(
//                this,
//                "${packageName}.fileprovider",
//                it
//            )
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//        }
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(150, 150)
            .cropSquare()
            .cameraOnly()
            .start()
    }

    private fun onFavoriteTap() {
//        TODO("Not yet implemented")
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                REQUEST_IMAGE_CAPTURE -> {
//                    binding.search.text?.clear()
//                    supportFragmentManager.beginTransaction().apply {
//                        replace(
//                            binding.placeholder.id,
//                            SearchImageFragment.newInstance(currentPhotoPath),
//                            FRAGMENT_TAG_IMAGE
//                        )
//                        commit()
//                    }
//                }
//            }
//        }
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                _binding.rvListFood.visibility = View.GONE
                val uri: Uri = data?.data!!
                _binding.search.text?.clear()
                supportFragmentManager.beginTransaction().apply {
                    replace(
                        _binding.placeholder.id,
                        SearchImageFragment.newInstance(uri.path ?: ""),
                        FRAGMENT_TAG_IMAGE
                    )
                    commit()
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onClick(v: View) {
        val popup = PopupMenu(this, v)
        val inflater : MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.options_change_language -> {startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))}
            }
            true
        }
        popup.show()
    }

}
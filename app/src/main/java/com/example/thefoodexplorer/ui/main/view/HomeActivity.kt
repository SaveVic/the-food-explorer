package com.example.thefoodexplorer.ui.main.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import com.example.thefoodexplorer.util.ApiResponseType
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*

enum class ListStatus{ LOADING, FILLED, EMPTY, ERROR }

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var _binding: ContentHomeBinding
    private lateinit var viewModel: HomeViewModel

    private var initialPage = true
    private var stateAllFood = ListStatus.LOADING

    private var lastQuery: String? = null
    private var lastImage: Intent? = null

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
        setupAllFood()
    }

    private fun setupAllFood() {
        val foodListAdapter = FoodListAdapter()
        _binding.rvListFood.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = foodListAdapter
        }
        showHideAllFood(show = true)
        viewModel.allFoods.observe(this, { response ->
            val cnt = response.data?.size ?: 0
            val type = response.type
            stateAllFood = when{
                type == ApiResponseType.ERROR -> ListStatus.ERROR
                type == ApiResponseType.SUCCESS && cnt == 0 -> ListStatus.EMPTY
                type == ApiResponseType.SUCCESS && cnt > 0 -> ListStatus.FILLED
                else -> ListStatus.LOADING
            }
            foodListAdapter.setFood(response.data)
            foodListAdapter.notifyDataSetChanged()
            if(initialPage) showHideAllFood(show = true)
        })
    }

    private fun showHideAllFood(show: Boolean = true) {
        if (show) {
            when (stateAllFood) {
                ListStatus.LOADING -> {
                    _binding.loading.visibility = View.VISIBLE
                    _binding.empty.root.visibility = View.GONE
                    _binding.rvListFood.visibility = View.GONE
                }
                ListStatus.FILLED -> {
                    _binding.loading.visibility = View.GONE
                    _binding.empty.root.visibility = View.GONE
                    _binding.rvListFood.visibility = View.VISIBLE
                }
                else -> {
                    _binding.loading.visibility = View.GONE
                    _binding.empty.root.visibility = View.VISIBLE
                    _binding.rvListFood.visibility = View.GONE
                }
            }
        }
        else{
            _binding.loading.visibility = View.GONE
            _binding.empty.root.visibility = View.GONE
            _binding.rvListFood.visibility = View.GONE
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
                    onSearchFoodByText((_binding.search.text ?: "").toString())
                    false
                }
                else -> false
            }
        }
    }

    private fun onSearchFoodByText(query: String) {
//        val query = (_binding.search.text ?: "").toString()
        showHideAllFood(show = false)
        if (query.isEmpty()) return
        lastQuery = query
        lastImage = null
        supportFragmentManager.beginTransaction().apply {
            replace(
                _binding.placeholder.id,
                SearchTextFragment.newInstance(query),
                FRAGMENT_TAG_QUERY
            )
            if (initialPage) {
                initialPage = false
                addToBackStack(null)
            }
            commit()
        }
    }

    private fun onSearchFoodByImage(data: Intent?) {
        val uri: Uri = data?.data!!
        _binding.search.text?.clear()
        lastQuery = null
        lastImage = data
        showHideAllFood(show = false)
        supportFragmentManager.beginTransaction().apply {
            replace(
                _binding.placeholder.id,
                SearchImageFragment.newInstance(uri.path ?: ""),
                FRAGMENT_TAG_IMAGE
            )
            if (initialPage) {
                initialPage = false
                addToBackStack(null)
            }
            commit()
        }
    }

    private fun onCameraTap() {
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
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                onSearchFoodByImage(data)
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
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.options_change_language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                }
                R.id.options_refresh -> {
                    when{
                        lastQuery != null -> onSearchFoodByText(lastQuery!!)
                        lastImage != null -> onSearchFoodByImage(lastImage)
                        else -> viewModel.refreshAllFoods()
                    }
                }
            }
            true
        }
        popup.show()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        Log.e("Banyak frag", count.toString())

        if (count == 0) {
            super.onBackPressed()
        } else {
            super.onBackPressed()
            _binding.placeholder.removeAllViews()
            initialPage = true
            lastQuery = null
            lastImage = null
            showHideAllFood(show = true)
            _binding.search.text?.clear()
        }
    }

}
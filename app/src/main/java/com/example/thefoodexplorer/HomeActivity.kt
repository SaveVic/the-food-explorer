package com.example.thefoodexplorer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.fragment.SearchImageFragment
import com.example.thefoodexplorer.fragment.SearchTextFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.favorite.setOnClickListener { onFavoriteTap() }
        binding.camera.setOnClickListener { onCameraTap() }
        binding.search.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onSearchFoodByText()
                    true
                }
                else -> false
            }
        }
    }



    private fun onSearchFoodByText() {
//        TODO("Not yet implemented")
        supportFragmentManager.beginTransaction().apply {
            replace(binding.placeholder.id, SearchTextFragment.newInstance(), "search-text")
            addToBackStack(null)
            commit()
        }
    }

    private fun onCameraTap() {
//        TODO("Not yet implemented")
        supportFragmentManager.beginTransaction().apply {
            replace(binding.placeholder.id, SearchImageFragment(), "search-image")
            addToBackStack(null)
            commit()
        }
    }

    private fun onFavoriteTap() {
        TODO("Not yet implemented")
    }


}
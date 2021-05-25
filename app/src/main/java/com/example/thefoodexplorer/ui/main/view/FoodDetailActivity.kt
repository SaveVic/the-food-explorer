package com.example.thefoodexplorer.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ActivityFoodDetailBinding
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.ui.base.ViewModelFactory
import com.example.thefoodexplorer.ui.main.viewmodel.DetailViewModel
import com.example.thefoodexplorer.ui.main.viewmodel.HomeViewModel

class FoodDetailActivity : AppCompatActivity() {
    companion object{
        const val DATA = "food-data"
    }

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var data : FoodQuery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            data = intent.getSerializableExtra(DATA) as FoodQuery
        }catch (e: Exception){}

        setupViewModel()
        setupUI()
        setupObserver()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this@FoodDetailActivity,
            ViewModelFactory.getInstance()
        )[DetailViewModel::class.java]
    }

    private fun setupUI() {
        binding.back.setOnClickListener { finish() }
        binding.btnDetail.setOnClickListener{ viewModel.setDetailUI() }
        binding.btnLocation.setOnClickListener{ viewModel.setLocationUI() }
        data?.let { food ->
            Glide.with(this)
                .load(food.image)
                .into(binding.image)
            binding.name.text = food.name
            binding.city.text = food.city
        }
    }

    private fun setupObserver() {
        viewModel.getDetailStatus().observe(this, {
            setButtonView(binding.btnDetail, it)
        })
        viewModel.getLocationStatus().observe(this, {
            setButtonView(binding.btnLocation, it)
        })
    }

    private fun setButtonView(btn: AppCompatButton, active: Boolean){
        when(active){
            true -> {
                btn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.normal_blur))
                btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.dark))
            }
            else -> {
                btn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
                btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.medium_blur))
            }
        }
    }
}
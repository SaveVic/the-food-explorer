package com.example.thefoodexplorer.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ActivityFoodDetailBinding
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.ui.base.ViewModelFactory
import com.example.thefoodexplorer.ui.main.adapter.FoodIngredientTagsAdapter
import com.example.thefoodexplorer.ui.main.adapter.FoodQueryListAdapter
import com.example.thefoodexplorer.ui.main.adapter.FoodTasteTagsAdapter
import com.example.thefoodexplorer.ui.main.viewmodel.DetailViewModel
import com.example.thefoodexplorer.ui.main.viewmodel.HomeViewModel
import com.example.thefoodexplorer.util.ApiResponseType

class FoodDetailActivity : AppCompatActivity() {
    companion object{
        const val DATA = "food-data"
    }

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var data : FoodQuery? = null
    private lateinit var adapterIngredient: FoodIngredientTagsAdapter
    private lateinit var adapterTaste: FoodTasteTagsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            data = intent.getSerializableExtra(DATA) as FoodQuery
        }catch (e: Exception){}

        setupViewModel()
        setupUI()
        setupAdapter()
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
                .apply(RequestOptions().override(150, 150))
                .centerCrop()
                .into(binding.image)
            binding.name.text = food.name
            binding.city.text = food.city
        }
    }

    private fun setupAdapter(){
        binding.rvIngredients.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapterIngredient = FoodIngredientTagsAdapter(arrayListOf())
        binding.rvIngredients.adapter = adapterIngredient

        binding.rvTaste.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapterTaste = FoodTasteTagsAdapter(arrayListOf())
        binding.rvTaste.adapter = adapterTaste
    }

    private fun setupObserver() {
        viewModel.getDetailStatus().observe(this, {
            setButtonView(binding.btnDetail, it)
            if(it){
                binding.infoDetail.visibility = View.VISIBLE
            }else binding.infoDetail.visibility = View.GONE
        })
        viewModel.getLocationStatus().observe(this, {
            setButtonView(binding.btnLocation, it)
        })
        viewModel.getDetailFood().observe(this, {
            if(it.type == ApiResponseType.SUCCESS){
                it.data?.let { detail ->
                    binding.desc.text = detail.desc
                    adapterIngredient.replaceList(detail.ingredient)
                    adapterIngredient.notifyDataSetChanged()
                    adapterTaste.replaceList(detail.taste)
                    adapterTaste.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setButtonView(btn: AppCompatButton, active: Boolean){
        when(active){
            true -> {
                btn.setBackgroundResource(R.drawable.bg_detail_button)
                btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.dark))
            }
            else -> {
                btn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
                btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.medium_blur))
            }
        }
        btn.setPadding(38, 18,38,18)
    }
}
package com.example.thefoodexplorer.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.data.model.FoodLocation
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ActivityFoodDetailBinding
import com.example.thefoodexplorer.databinding.ContentDetailBinding
import com.example.thefoodexplorer.ui.base.ViewModelFactory
import com.example.thefoodexplorer.ui.main.adapter.FoodIngredientTagsAdapter
import com.example.thefoodexplorer.ui.main.adapter.FoodLocationGridAdapter
import com.example.thefoodexplorer.ui.main.adapter.FoodTasteTagsAdapter
import com.example.thefoodexplorer.ui.main.viewmodel.DetailViewModel
import com.example.thefoodexplorer.util.ApiResponseType
import com.google.android.material.snackbar.Snackbar

class FoodDetailActivity : AppCompatActivity() {
    companion object{
        const val DATA = "food-data"
    }

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var _binding: ContentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var data : FoodQuery? = null
    private lateinit var adapterIngredient: FoodIngredientTagsAdapter
    private lateinit var adapterTaste: FoodTasteTagsAdapter
    private lateinit var adapterLocation: FoodLocationGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        _binding = binding.contentDetail
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
        _binding.back.setOnClickListener { finish() }
        _binding.btnDetail.setOnClickListener{ viewModel.setDetailUI() }
        _binding.btnLocation.setOnClickListener{ viewModel.setLocationUI() }
        data?.let { food ->
            Glide.with(this)
                .load(food.image)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(_binding.image)
            _binding.name.text = food.name
            _binding.city.text = food.city
            viewModel.setFoodID(food.id)
        }
    }

    private fun setupAdapter(){
       _binding.rvIngredients.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapterIngredient = FoodIngredientTagsAdapter(arrayListOf())
        _binding.rvIngredients.adapter = adapterIngredient

        _binding.rvTaste.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapterTaste = FoodTasteTagsAdapter(arrayListOf())
        _binding.rvTaste.adapter = adapterTaste

        _binding.rvLocation.visibility = View.GONE
        _binding.shimmerLocation.visibility = View.VISIBLE
        _binding.rvLocation.layoutManager = GridLayoutManager(this, 2)
        adapterLocation = FoodLocationGridAdapter(arrayListOf())
        _binding.rvLocation.adapter = adapterLocation
        adapterLocation.setOnItemClickCallback(object: FoodLocationGridAdapter.OnItemClickCallback{
            override fun onItemClicked(location: FoodLocation) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(location.url)
                startActivity(intent)
            }
        })
    }

    private fun setupObserver() {
        viewModel.getDetailStatus().observe(this, { active ->
            setButtonView(_binding.btnDetail, active)
            if(active){
                _binding.infoDetail.visibility = View.VISIBLE
            }else _binding.infoDetail.visibility = View.GONE
        })
        viewModel.getLocationStatus().observe(this, { active ->
            setButtonView(_binding.btnLocation, active)
            if(active){
                _binding.infoLocation.visibility = View.VISIBLE
            }else _binding.infoLocation.visibility = View.GONE
        })
        viewModel.getDetailFood().observe(this, {
            if (it.type == ApiResponseType.SUCCESS || it.type == ApiResponseType.ERROR) {
                _binding.shimmerDesc.stopShimmerAnimation()
                _binding.shimmerIngredients.stopShimmerAnimation()
                _binding.shimmerTaste.stopShimmerAnimation()

                _binding.shimmerDesc.visibility = View.GONE
                _binding.shimmerIngredients.visibility = View.GONE
                _binding.shimmerTaste.visibility = View.GONE

                _binding.desc.visibility = View.VISIBLE
                _binding.rvIngredients.visibility = View.VISIBLE
                _binding.rvTaste.visibility = View.VISIBLE

                it.data?.let { detail ->
                    _binding.desc.text = detail.desc
                    adapterIngredient.replaceList(detail.ingredient)
                    adapterIngredient.notifyDataSetChanged()
                    adapterTaste.replaceList(detail.taste)
                    adapterTaste.notifyDataSetChanged()
                }
            }
            if(it.type == ApiResponseType.ERROR){
                val errorMsg = resources.getString(R.string.msg_list_error)
                val err = it.msg ?: ""
                val msg = "$errorMsg\n$err"
                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getLocationFood().observe(this, {
            if(it.type != ApiResponseType.LOADING){
                _binding.rvLocation.visibility = View.VISIBLE
                _binding.shimmerLocation.stopShimmerAnimation()
                _binding.shimmerLocation.visibility = View.GONE
                it.data?.let { list ->
                    adapterLocation.replaceList(list)
                    adapterLocation.notifyDataSetChanged()
                }
            }
            if(it.type == ApiResponseType.ERROR){
                val errorMsg = resources.getString(R.string.msg_list_error)
                val err = it.msg ?: ""
                val msg = "$errorMsg\n$err"
                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        _binding.shimmerDesc.startShimmerAnimation()
        _binding.shimmerIngredients.startShimmerAnimation()
        _binding.shimmerTaste.startShimmerAnimation()
        _binding.shimmerLocation.startShimmerAnimation()
    }

    override fun onPause() {
        _binding.shimmerDesc.stopShimmerAnimation()
        _binding.shimmerIngredients.stopShimmerAnimation()
        _binding.shimmerTaste.stopShimmerAnimation()
        _binding.shimmerLocation.stopShimmerAnimation()
        super.onPause()
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
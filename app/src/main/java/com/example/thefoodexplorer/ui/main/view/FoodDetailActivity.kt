package com.example.thefoodexplorer.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.thefoodexplorer.R

class FoodDetailActivity : AppCompatActivity() {
    companion object{
        const val DATA = "food-data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)
    }
}
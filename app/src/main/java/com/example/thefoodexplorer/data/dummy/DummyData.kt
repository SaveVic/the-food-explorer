package com.example.thefoodexplorer.data.dummy

import com.example.thefoodexplorer.data.model.FoodQuery

object DummyData {
    fun getFoods() = listOf(
        FoodQuery(
            0,
            "Food A",
            "Kota A",
            "https://cdns.klimg.com/merdeka.com/i/w/news/2020/07/02/1193888/540x270/cara-pembuatan-tahu-ala-rumahan-dan-aneka-olahannya.jpeg",
        ),
        FoodQuery(
            1,
            "Food B",
            "Kota B",
            "https://awsimages.detik.net.id/community/media/visual/2021/05/18/tempe-dari-indomie-1_43.jpeg?w=700&q=90",
        ),
        FoodQuery(
            2,
            "Food C",
            "Kota C",
            "data:image/jpeg;base64,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",
        ),
        FoodQuery(
            3,
            "Food C",
            "Kota C",
            "https://asset.kompas.com/crops/UeT3ywkCaq37WhD3HfVSNCR1YBg=/17x10:700x465/750x500/data/photo/2021/02/03/601a709d7cd47.jpg",
        ),
    )
}
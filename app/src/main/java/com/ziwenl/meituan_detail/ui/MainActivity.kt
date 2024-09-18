package com.ziwenl.meituan_detail.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziwenl.meituan_detail.ui.shop.ShopDetailsActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        com.ziwenl.meituan_detail.databinding.ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnOpen.setOnClickListener {
            startActivity(Intent(this, ShopDetailsActivity::class.java))
        }
    }
}
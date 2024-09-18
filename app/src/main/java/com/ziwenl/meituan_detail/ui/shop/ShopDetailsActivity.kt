package com.ziwenl.meituan_detail.ui.shop

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ziwenl.meituan_detail.R


/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 10:49
 * Introduction : 店铺详情
 */
class ShopDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_shop_details)
    }
}
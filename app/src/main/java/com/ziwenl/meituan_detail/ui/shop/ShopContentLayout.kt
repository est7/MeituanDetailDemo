package com.ziwenl.meituan_detail.ui.shop

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.ziwenl.meituan_detail.databinding.LayoutShopDetailsContentBinding
import com.ziwenl.meituan_detail.ui.shop.adapter.ViewPagerAdapter
import com.ziwenl.meituandemo.ui.store.EvaluateFragment
import com.ziwenl.meituandemo.ui.store.ShopFragment

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 11:04
 * Introduction : 店铺详情 -- 中下部分主内容（点菜/评价/商家）
 */
class ShopContentLayout : ConstraintLayout {

    private lateinit var binding: LayoutShopDetailsContentBinding
    private lateinit var mFragmentList: MutableList<Fragment>
    private var mShopContentBehavior: ShopContentBehavior? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        binding = LayoutShopDetailsContentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //点菜、评价、商家 Fragment
        mFragmentList = mutableListOf()
        mFragmentList.add(MenuFragment.getInstance(object : MenuFragment.LayoutExpandControl {
            override fun fold() {
                mShopContentBehavior?.fold()
            }

            override fun isExpanded(): Boolean {
                return mShopContentBehavior?.isExpanded() ?: false
            }
        }))
        mFragmentList.add(EvaluateFragment.getInstance())
        mFragmentList.add(ShopFragment.getInstance())
        val vpAdapter =
            ViewPagerAdapter((context as AppCompatActivity).supportFragmentManager, mFragmentList)
        binding.vpMain.adapter = vpAdapter
        binding.vpMain.offscreenPageLimit = mFragmentList.size

        binding.tabLayout.setViewPager(binding.vpMain, arrayOf("点菜", "评价", "商家"))
    }

    fun getScrollableView(): View {
        val view =
            (mFragmentList[binding.vpMain.currentItem] as ScrollableViewProvider).getScrollableView()
        return view
    }

    fun getRootScrollView(): View? {
        return (mFragmentList[binding.vpMain.currentItem] as ScrollableViewProvider).getRootScrollView()
    }

    fun setShopContentBehavior(shopContentBehavior: ShopContentBehavior) {
        mShopContentBehavior = shopContentBehavior
    }
}

interface ScrollableViewProvider {
    fun getScrollableView(): View
    fun getRootScrollView(): View? {
        return null
    }
}
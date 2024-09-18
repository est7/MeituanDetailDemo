package com.ziwenl.meituan_detail.ui.shop

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.databinding.LayoutShopPriceBinding
import com.ziwenl.meituan_detail.utils.dp
import com.ziwenl.meituan_detail.utils.stateRefresh
import com.ziwenl.meituan_detail.utils.stateSave

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/11/19
 * Time : 16:18
 * Introduction : 店铺详情 -- 底部价格(满减神器、满减优惠、结算价格)
 */
class ShopPriceLayout : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: LayoutShopPriceBinding

    private fun animViews(): Array<View> = arrayOf(binding.clMain)

    init {
        binding = LayoutShopPriceBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var firstLayout: Boolean = false
    private var effected: Float = 0f

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            //记录 View 状态
            binding.clMain.stateSave(R.id.viewStateStart).alpha(1F)
            binding.clMain.stateSave(R.id.viewStateEnd).alpha(0F)
        }
    }

    /**
     * 根据业务需求动态计算变化高度区别
     */
    fun effectByOffset(transY: Float) {
        effected = when {
            transY <= dp(140) -> 0F
            transY > dp(140) && transY < dp(230) -> (transY - dp(140)) / dp(90)
            else -> 1F
        }
        animViews().forEach { it.stateRefresh(R.id.viewStateStart, R.id.viewStateEnd, effected) }
    }

    private var mIsExpanded = false
}
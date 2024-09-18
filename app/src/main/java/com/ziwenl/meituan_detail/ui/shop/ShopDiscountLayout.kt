package com.ziwenl.meituan_detail.ui.shop

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.databinding.LayoutShopDetailsDiscountBinding
import com.ziwenl.meituan_detail.databinding.WidgetShopDetailsDiscountBinding
import com.ziwenl.meituan_detail.databinding.WidgetShopDetailsDiscountExpandedBinding
import com.ziwenl.meituan_detail.utils.dp
import com.ziwenl.meituan_detail.utils.stateRefresh
import com.ziwenl.meituan_detail.utils.stateSave
import com.ziwenl.meituan_detail.utils.statesChangeByAnimation

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 10:57
 * Introduction : 店铺详情 -- 顶部折扣活动布局
 */
class ShopDiscountLayout : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    private var mIsExpanded = false
    private lateinit var binding: LayoutShopDetailsDiscountBinding
    private lateinit var includedBinding: WidgetShopDetailsDiscountBinding
    private lateinit var includedExpandBinding: WidgetShopDetailsDiscountExpandedBinding

    private fun animViews(): Array<View> = arrayOf(
        includedExpandBinding.tvShopNameB,
        includedBinding.clDiscount,
        includedExpandBinding.clDiscountExpanded,
        binding.viewTopBgShadow
    )

    private var effected: Float = 0f
    private var firstLayout: Boolean = false

    private val internalAnimListener: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            animListener?.onAnimationStart(animation, mIsExpanded)
        }
    }

    var animListener: AnimatorListenerAdapter1? = null

    init {
        binding = LayoutShopDetailsDiscountBinding.inflate(LayoutInflater.from(context), this, true)
        includedBinding =
            WidgetShopDetailsDiscountBinding.bind(binding.root.findViewById(R.id.widget_shop_details_discount))
        includedExpandBinding =
            WidgetShopDetailsDiscountExpandedBinding.bind(binding.root.findViewById(R.id.widget_shop_details_discount_expanded))
        binding.svMain.setOnTouchListener { _, _ -> !mIsExpanded }

        includedBinding.tvAnnouncement.setOnClickListener { switch(!mIsExpanded) }
        binding.ivClose.setOnClickListener { switch(!mIsExpanded) }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            //记录 View 状态
            binding.viewTopBgShadow.stateSave(R.id.viewStateStart).alpha(0F)
            binding.viewTopBgShadow.stateSave(R.id.viewStateEnd).alpha(1F)
            includedExpandBinding.tvShopNameB.stateSave(R.id.viewStateStart).alpha(0.8F)
            includedExpandBinding.tvShopNameB.stateSave(R.id.viewStateEnd).alpha(1F)
            includedBinding.clDiscount.stateSave(R.id.viewStateStart).alpha(1F)
            includedBinding.clDiscount.stateSave(R.id.viewStateEnd).alpha(0F)
            includedExpandBinding.clDiscountExpanded.stateSave(R.id.viewStateStart).alpha(0F)
            includedExpandBinding.clDiscountExpanded.stateSave(R.id.viewStateEnd).alpha(1F)
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

    /**
     * 展开/收缩当前布局
     */
    fun switch(
        expanded: Boolean, byScrollerSlide: Boolean = false
    ) {
        if (mIsExpanded == expanded) {
            return
        }
        binding.svMain.scrollTo(0, 0)
        mIsExpanded = expanded // 目标
        val start = effected
        val end = if (expanded) 1F else 0F
        statesChangeByAnimation(
            animViews(),
            R.id.viewStateStart,
            R.id.viewStateEnd,
            start,
            end,
            null,
            if (!byScrollerSlide) internalAnimListener else null,
            500
        )
    }

    interface AnimatorListenerAdapter1 {
        fun onAnimationStart(animation: Animator?, toExpanded: Boolean)
    }
}
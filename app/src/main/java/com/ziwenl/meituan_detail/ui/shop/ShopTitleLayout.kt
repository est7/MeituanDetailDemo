package com.ziwenl.meituan_detail.ui.shop

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.databinding.LayoutShopDetailsTitleBinding
import com.ziwenl.meituan_detail.utils.SystemUtil
import com.ziwenl.meituan_detail.utils.argbEvaluator
import com.ziwenl.meituan_detail.utils.dp
import com.ziwenl.meituan_detail.utils.resDimension
import com.ziwenl.meituan_detail.utils.resDrawable

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 10:58
 * Introduction : 店铺详情 -- 标题栏
 */
class ShopTitleLayout : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: LayoutShopDetailsTitleBinding

    private val offsetMax = resDimension(R.dimen.top_min_height).toFloat()
    private val adInterpolator = AccelerateDecelerateInterpolator()
    private var offset = 0F
    private val drawableBack = resDrawable(R.mipmap.back_white)
    private val drawableSearch = resDrawable(R.mipmap.icon_search)
    private val drawableCollection = resDrawable(R.mipmap.icon_collection)
    private val drawableMenu = resDrawable(R.mipmap.icon_menu)

    init {
        binding = LayoutShopDetailsTitleBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var mStatusBarIsDark = false

    fun effectByOffset(dy: Int): Float {
        if (dy > 0 && offset == offsetMax) return 1F
        else if (dy < 0 && offset == 0F) return 0F

        offset += dy
        if (offset > offsetMax) offset = offsetMax
        else if (offset < 0) offset = 0F

        val effect = adInterpolator.getInterpolation(offset / offsetMax)

        setBackgroundColor(
            argbEvaluator.evaluate(
                effect,
                Color.TRANSPARENT,
                0xFFFFFFFF.toInt()
            ) as Int
        )

        val e: Int = argbEvaluator.evaluate(effect, Color.WHITE, 0xFF646464.toInt()) as Int
        binding.ivBack.setImageDrawable(tintDrawable(drawableBack, ColorStateList.valueOf(e)))
        binding.ivMenu.setImageDrawable(tintDrawable(drawableMenu, ColorStateList.valueOf(e)))
        binding.ivCollection.setImageDrawable(tintDrawable(drawableCollection, ColorStateList.valueOf(e)))
        binding.ivSearch.setImageDrawable(tintDrawable(drawableSearch, ColorStateList.valueOf(e)))

        binding.ivSearch.scaleX = (1 - 0.4 * effect).toFloat()
        binding.ivSearch.scaleY = (1 - 0.4 * effect).toFloat()
        binding.ivSearch.translationX = -(binding.tvSearch.width - binding.ivSearch.width + dp(3)) * effect
        binding.tvSearch.alpha = effect
        binding.tvSearch.pivotX = binding.tvSearch.width.toFloat()
        binding.tvSearch.scaleX = (0.2 + 0.8 * effect).toFloat()

        //根据滚动比例动态设置状态字体颜色为深色
        if ((effect >= 0.5f) != mStatusBarIsDark) {
            mStatusBarIsDark = !mStatusBarIsDark
            SystemUtil.setStatusBarDarkMode((context as Activity), mStatusBarIsDark)
        }
        return effect
    }

    private fun tintDrawable(drawable: Drawable, colors: ColorStateList): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }
}
package com.ziwenl.meituan_detail.ui.shop

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.databinding.TicketViewBinding
import com.ziwenl.meituan_detail.utils.AnimationUpdateListener
import com.ziwenl.meituan_detail.utils.stateRefresh
import com.ziwenl.meituan_detail.utils.stateSave

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 16:05
 * Introduction :
 */
class TicketView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs),
    AnimationUpdateListener {

    private var firstLayout: Boolean = false
    private val binding: TicketViewBinding

    init {
        binding = TicketViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            binding.vBorder1.stateSave(R.id.viewStateStart).alpha(1F)
            binding.vBorder1.stateSave(R.id.viewStateEnd).ws(3.8F).hs(3.8F).alpha(0F)
            binding.vBorder2.stateSave(R.id.viewStateStart).alpha(0F)
            binding.vBorder2.stateSave(R.id.viewStateEnd).ws(3.8F).hs(3.8F).alpha(1F)
            binding.vSimple.stateSave(R.id.viewStateStart)
            binding.vSimple.stateSave(R.id.viewStateEnd).alpha(0f)
            binding.layDetail.stateSave(R.id.viewStateStart)
            binding.layDetail.stateSave(R.id.viewStateEnd).scaleX(1F).scaleY(1F).alpha(1F)
        }
    }

    fun set(amount: Int, limit: Int, expireTime: String) {
        binding.vSimple.text = "领￥$amount"

        binding.vDetail1.text = "￥$amount"
        binding.vDetail2.text = "满$limit 可用"
        binding.vDetail3.text = "有效期至$expireTime"
    }

    override fun onAnimationUpdate(tag1: Int, tag2: Int, p: Float) {
        arrayOf(binding.vBorder1, binding.vBorder2, binding.vSimple, binding.layDetail).forEach { it.stateRefresh(tag1, tag2, p) }
    }
}
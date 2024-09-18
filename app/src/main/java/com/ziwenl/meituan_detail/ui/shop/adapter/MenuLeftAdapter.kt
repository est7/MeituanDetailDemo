package com.ziwenl.meituandemo.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.databinding.ItemShopDetailsMenuLeftBinding
import com.ziwenl.meituandemo.bean.MenuTabBean

class MenuLeftAdapter(
    private val data: MutableList<MenuTabBean>
) : RecyclerView.Adapter<MenuLeftAdapter.ViewHolder>() {

    var currentPosition = 0

    class ViewHolder(val binding: ItemShopDetailsMenuLeftBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopDetailsMenuLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            cbName.isChecked = position == currentPosition
            cbName.text = data[position].name
            cbName.setOnClickListener {
                mCallBack?.onClickItem(position)
            }
            viewBottom.visibility = if (position == data.size - 1) {
                ViewGroup.VISIBLE
            } else {
                ViewGroup.GONE
            }
        }
    }

    private var mCallBack: Callback? = null

    fun setCallback(callback: Callback) {
        mCallBack = callback
    }

    interface Callback {
        fun onClickItem(position: Int)
    }
}
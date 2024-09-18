package com.ziwenl.meituandemo.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.databinding.ItemShopDetailsMenuRightBinding
import com.ziwenl.meituandemo.bean.MenuChildBean

/**
 * PackageName : com.ziwenl.meituandemo.ui.store.adapter
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 16:26
 * Introduction :
 */
class MenuRightAdapter(private val data: MutableList<MenuChildBean>) : RecyclerView.Adapter<MenuRightAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemShopDetailsMenuRightBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopDetailsMenuRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.viewBottom.visibility = if (position == data.size - 1) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }
}
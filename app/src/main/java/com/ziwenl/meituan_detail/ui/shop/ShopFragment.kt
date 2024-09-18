package com.ziwenl.meituandemo.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziwenl.meituan_detail.databinding.FragmentShopDetailsShopBinding
import com.ziwenl.meituan_detail.ui.shop.ScrollableViewProvider

/**
 * PackageName : com.ziwenl.meituandemo.ui.store
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 10:18
 * Introduction :商家
 */
class ShopFragment : Fragment(), ScrollableViewProvider {

    private var _binding: FragmentShopDetailsShopBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun getInstance(): Fragment {
            return ShopFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopDetailsShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getScrollableView(): View {
        return  binding.svMain
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
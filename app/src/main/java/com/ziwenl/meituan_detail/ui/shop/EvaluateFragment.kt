package com.ziwenl.meituandemo.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziwenl.meituan_detail.databinding.FragmentStoreDetailsEvaluateBinding
import com.ziwenl.meituan_detail.ui.shop.ScrollableViewProvider

class EvaluateFragment : Fragment(), ScrollableViewProvider {

    private var _binding: FragmentStoreDetailsEvaluateBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun getInstance(): EvaluateFragment {
            val fragment = EvaluateFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreDetailsEvaluateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getScrollableView(): View {
        return binding.svMain
    }
}
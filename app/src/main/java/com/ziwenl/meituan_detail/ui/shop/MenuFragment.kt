package com.ziwenl.meituan_detail.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.databinding.FragmentShopDetailsMenuBinding
import com.ziwenl.meituan_detail.ui.shop.adapter.FloatDecoration
import com.ziwenl.meituan_detail.widgets.CenterLayoutManager
import com.ziwenl.meituandemo.bean.MenuChildBean
import com.ziwenl.meituandemo.bean.MenuTabBean
import com.ziwenl.meituandemo.ui.store.adapter.MenuLeftAdapter
import com.ziwenl.meituandemo.ui.store.adapter.MenuRightAdapter


/**
 * PackageName : com.ziwenl.meituandemo.ui.store
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 10:15
 * Introduction : 点菜（菜单）
 */
class MenuFragment : Fragment(), ScrollableViewProvider {
    private val mLeftData = mutableListOf<MenuTabBean>()
    private val mRightData = mutableListOf<MenuChildBean>()
    private lateinit var mLeftAdapter: MenuLeftAdapter
    private lateinit var mLayoutControl: LayoutExpandControl
    private var mIsClickFold = false
    private var mRvState = RecyclerView.State()
    private lateinit var mLeftLayoutManager: CenterLayoutManager

    private var _binding: FragmentShopDetailsMenuBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun getInstance(
            layoutExpandControl: LayoutExpandControl
        ): MenuFragment {
            val fragment = MenuFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.setLayoutExpandControl(layoutExpandControl)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopDetailsMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //初始化
        mLeftAdapter = MenuLeftAdapter(mLeftData)
        mLeftLayoutManager = CenterLayoutManager(requireContext())
        binding.rvLeft.layoutManager = mLeftLayoutManager
        binding.rvLeft.adapter = mLeftAdapter
        val rightAdapter = MenuRightAdapter(mRightData)
        binding.rvRight.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRight.adapter = rightAdapter
        //左边 RecyclerView item 点击事件监听
        mLeftAdapter.setCallback(object : MenuLeftAdapter.Callback {
            override fun onClickItem(position: Int) {
                for (i in 0 until mRightData.size) {
                    if (mLeftData[position].name == mRightData[i].groupName) {
                        //未折叠时进行折叠
                        if (mLayoutControl.isExpanded()) {
                            mLayoutControl.fold()
                            mIsClickFold = true
                        }
                        if (binding.rvRight.layoutParams.height != binding.scrollView.height) {
                            binding.rvRight.layoutParams.height = binding.scrollView.height
                            binding.rvLeft.layoutParams.height = binding.scrollView.height
                            binding.rvRight.layoutParams = binding.rvRight.layoutParams
                            binding.rvLeft.layoutParams = binding.rvLeft.layoutParams
                        }
                        //右边菜品 RecyclerView 将指定 item 滚动到可见第一条
                        (binding.rvRight.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            i, 0
                        )
                        mLeftAdapter.currentPosition = position
                        mLeftAdapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })

        //右边 RecyclerView 添加悬浮吸顶装饰
        binding.rvRight.addItemDecoration(
            FloatDecoration(requireContext(),
                binding.rvRight,
                R.layout.item_shop_details_menu_right_group,
                object : FloatDecoration.DecorationCallback {
                    override fun getDecorationFlag(position: Int): String {
                        //区分不同条目装饰 View 的 Flag
                        return mRightData[position].groupName
                    }

                    override fun onBindView(decorationView: View, position: Int) {
                        //装饰 View 数据绑定
                        decorationView.findViewById<TextView>(R.id.tv_group_name).text = mRightData[position].groupName
                    }
                })
        )

        //右边 RecyclerView 滚动监听
        binding.rvRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //实现右边滚动联动左边 RecyclerView
                val position = (binding.rvRight.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (mLeftData[mLeftAdapter.currentPosition].name != mRightData[position].groupName) {
                    for (i in 0 until mLeftData.size) {
                        if (mLeftData[i].name == mRightData[position].groupName) {
                            mLeftAdapter.currentPosition = i
                            mLeftAdapter.notifyDataSetChanged()
                            mLeftLayoutManager.smoothScrollToPosition(
                                binding.rvLeft, mRvState, mLeftAdapter.currentPosition
                            )
                            break
                        }
                    }
                }
            }
        })
        //根据屏幕实际宽高设置两个recyclerView高度为固定值
        binding.scrollView.post {
            binding.rvRight.layoutParams.height = binding.scrollView.height
            binding.rvLeft.layoutParams.height = binding.scrollView.height
            binding.rvRight.layoutParams = binding.rvRight.layoutParams
            binding.rvLeft.layoutParams = binding.rvLeft.layoutParams
        }

        //记录是 rv_left 被触摸还是 rv_right 被触摸
        val onTouchListener = View.OnTouchListener { v, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN && v.id == R.id.rv_right -> {
                    mIsTouchRvRight = true
                }

                event.action == MotionEvent.ACTION_UP && v.id == R.id.rv_right -> {
                    mIsTouchRvRight = false
                }

                event.action == MotionEvent.ACTION_DOWN && v.id == R.id.rv_left -> {
                    mIsTouchRvLeft = true
                }

                event.action == MotionEvent.ACTION_UP && v.id == R.id.rv_left -> {
                    mIsTouchRvLeft = false
                }
            }
            false
        }
        binding.rvRight.setOnTouchListener(onTouchListener)
        binding.rvLeft.setOnTouchListener(onTouchListener)
        //scrollView 滚动监听
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val dy = scrollY - oldScrollY
            //上滑时，如果 bannerView 尚未被滚出屏幕，则不允许左右两个 RecyclerView 滚动 （通过 offsetChildrenVertical(dy) 实现两个 rv 未滚动的假象）
            if (dy > 0) {
                if (scrollY < binding.flBanner.height) {
                    if (mIsTouchRvRight) {
                        binding.rvRight.offsetChildrenVertical(dy)
                    }
                    if (mIsTouchRvLeft) {
                        binding.rvLeft.offsetChildrenVertical(dy)
                    }
                }
            }
        })
        //添加假数据
        getData()
    }

    private var mIsTouchRvRight = false
    private var mIsTouchRvLeft = false

    override fun getScrollableView(): View {
        //如果左/右边 recyclerView 还能往上滚，则将左/右边的 recyclerView 暴露出去用来判断滑动
        if (binding.rvRight.canScrollVertically(-1)) {
            return binding.rvRight
        } else if (binding.rvLeft.canScrollVertically(-1)) {
            return binding.rvLeft
        } else {
            return binding.scrollView
        }
    }

    override fun getRootScrollView(): View {
        return binding.scrollView
    }

    private fun getData() {
        //假数据
        mLeftData.add(MenuTabBean("收藏福利"))
        mLeftData.add(MenuTabBean("一人食"))
        mLeftData.add(MenuTabBean("新品尝鲜"))
        mLeftData.add(MenuTabBean("推荐"))
        mLeftData.add(MenuTabBean("折扣"))
        mLeftData.add(MenuTabBean("买一送一"))
        mLeftData.add(MenuTabBean("精选套餐"))
        mLeftData.add(MenuTabBean("企业团餐"))
        mLeftData.add(MenuTabBean("意面小吃"))
        mLeftData.add(MenuTabBean("下午时光"))
        mLeftData.add(MenuTabBean("卡券专用"))
        mLeftData.add(MenuTabBean("饮品"))
        mLeftData.add(MenuTabBean("收藏福利1"))
        mLeftData.add(MenuTabBean("一人食1"))
        mLeftData.add(MenuTabBean("新品尝鲜1"))
        mLeftData.add(MenuTabBean("推荐1"))
        mLeftData.add(MenuTabBean("折扣1"))
        mLeftData.add(MenuTabBean("买一送一1"))
        mLeftData.add(MenuTabBean("精选套餐1"))
        mLeftData.add(MenuTabBean("企业团餐1"))
        mLeftData.add(MenuTabBean("意面小吃1"))
        mLeftData.add(MenuTabBean("下午时光1"))
        mLeftData.add(MenuTabBean("卡券专用1"))
        mLeftData.add(MenuTabBean("饮品1"))
        for (i in 0 until mLeftData.size) {
            mRightData.add(MenuChildBean(mLeftData.get(i).name, ""))
            mRightData.add(MenuChildBean(mLeftData.get(i).name, ""))
            mRightData.add(MenuChildBean(mLeftData.get(i).name, ""))
        }
        binding.rvLeft.adapter?.notifyDataSetChanged()
        binding.rvRight.adapter?.notifyDataSetChanged()
    }

    private fun setLayoutExpandControl(layoutExpandControl: LayoutExpandControl) {
        mLayoutControl = layoutExpandControl
    }

    interface LayoutExpandControl {
        /**
         * 折叠布局
         */
        fun fold()

        /**
         * 布局是否展开
         */
        fun isExpanded(): Boolean
    }
}
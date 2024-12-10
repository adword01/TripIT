package com.example.tripit.fragments

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R
import com.example.tripit.viewmodels.PlaceDetailsViewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripit.HorizontalSpaceItemDecoration
import com.example.tripit.SCROLL_SLOW_FACTOR
import com.example.tripit.SlowScrollingLinearLayoutManager
import com.example.tripit.SlowScrollingPagerSnapHelper
import com.example.tripit.adapters.CardAdapter
import com.example.tripit.adapters.CardViewHolder
import com.example.tripit.viewmodels.Card
import kotlinx.coroutines.delay
import kotlin.math.floor


class PlaceDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaceDetailsFragment()
    }

    private val viewModel: PlaceDetailsViewModel by viewModels()
    private lateinit var recyclerView1: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_place_details, container, false)

        recyclerView1 = view.findViewById(R.id.recycler_view_1)

        setupRecyclerViews()

        lifecycleScope.launchWhenResumed {
            val order = listOf(0, 1, 2, 1, 0)
            var i = 0
            while (true) {
                val position = order[i % order.size]
                recyclerView1.smoothScrollToPosition(position)
                i++
                delay(floor(300 * SCROLL_SLOW_FACTOR).toLong())
            }
        }

        return view
    }

    private fun setupRecyclerViews() {
        recyclerView1.init(withParallax = true)
    }

    private fun RecyclerView.init(withParallax: Boolean) {
        adapter = CardAdapter(Card.MOCKED_ITEMS)
        layoutManager = SlowScrollingLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL)

        SlowScrollingPagerSnapHelper(context).attachToRecyclerView(this)

        val spacing = resources.getDimensionPixelOffset(R.dimen.spacing_card)
        addItemDecoration(HorizontalSpaceItemDecoration(spacing))

        if (withParallax) setupParallax()
    }

    private fun RecyclerView.setupParallax() {
        val viewBounds = Rect()
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = layoutManager as? LinearLayoutManager ?: return
                val scrollOffset = recyclerView.computeHorizontalScrollOffset()
                layoutManager.visiblePositions.forEach { position ->
                    val viewHolder = findViewHolderForAdapterPosition(position) as? CardViewHolder
                        ?: return@forEach

                    recyclerView.getDecoratedBoundsWithMargins(viewHolder.itemView, viewBounds)

                    val width = viewBounds.width().toFloat()
                    val viewStart = position * width
                    viewHolder.parallaxOffset = (viewStart - scrollOffset) / width
                }
            }
        })
    }

    private val LinearLayoutManager.visiblePositions: IntRange
        get() = (findFirstVisibleItemPosition()..findLastVisibleItemPosition())
}
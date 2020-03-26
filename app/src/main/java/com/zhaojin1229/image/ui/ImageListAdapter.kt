package com.zhaojin1229.image.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zhaojin1229.image.R
import com.zhaojin1229.image.databinding.ItemSearchBinding
import com.zhaojin1229.image.model.response.ImageResponse

class ImageListAdapter: RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {
    private lateinit var imgList: List<ImageResponse>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(::imgList.isInitialized) imgList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imgList[position])
    }

    fun updateSearchList(imgList: List<ImageResponse>){
        this.imgList = imgList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemSearchBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = SearchItemVM()

        fun bind(img: ImageResponse){
            viewModel.bind(img)
            binding.viewModel = viewModel
        }
    }

}
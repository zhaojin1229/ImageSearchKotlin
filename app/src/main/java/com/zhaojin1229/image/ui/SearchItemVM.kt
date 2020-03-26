package com.zhaojin1229.image.ui

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zhaojin1229.image.base.BaseViewModel
import com.zhaojin1229.image.model.response.ImageResponse


class SearchItemVM: BaseViewModel(){
     val url = MutableLiveData<String>()
     val thumbnail = MutableLiveData<String>()
     val name = MutableLiveData<String>()
     val title = MutableLiveData<String>()
     val imageWebSearchUrl = MutableLiveData<String>()

    fun bind(img: ImageResponse){
        url.value = img.url
        thumbnail.value = img.thumbnail
        name.value = img.name
        title.value = img.title
        imageWebSearchUrl.value = img.imageWebSearchUrl
    }

    fun onImgClicked(view: View){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =Uri.parse(url.value)
        view.context.startActivity(intent)
    }

}
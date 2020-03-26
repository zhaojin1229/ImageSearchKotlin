package com.zhaojin1229.image.ui

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener
import com.zhaojin1229.image.R
import com.zhaojin1229.image.base.BaseViewModel
import com.zhaojin1229.image.model.response.ImageData
import com.zhaojin1229.image.model.response.ImageResponse
import com.zhaojin1229.image.network.PostApi
import com.zhaojin1229.image.util.x
import com.zhaojin1229.image.util.y
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchListVM : BaseViewModel(){
    @Inject
    lateinit var postApi: PostApi

    val imgListAdapter: ImageListAdapter = ImageListAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val buttonEnable: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val searchContent: MutableLiveData<String> = MutableLiveData()
    private lateinit var content: String
    val errorClickListener = View.OnClickListener { loadPosts(content) }
    var imgListData: MutableList<ImageResponse> = mutableListOf()
    var listLoadListener: LoadingListener
    var page: MutableLiveData<Int> = MutableLiveData(1)

    private lateinit var subscription: Disposable
    private lateinit var d1: Disposable

    init{
        listLoadListener = object : LoadingListener {
            override fun onRefresh() {
                Handler().postDelayed({
                    page.value = 1
                    imgListData.clear()
                    loadPosts(content)
                }, 500)
            }

            override fun onLoadMore() {
                Handler().postDelayed({
                    page.value = page.value!!+1
                    loadPosts(content)
                }, 500)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadPosts(content:String?){
        content?.let {
            subscription =postApi.getPosts(x, y,page.value?:1,10,it,false).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                    { result -> onRetrievePostListSuccess(result) },
                    { throwable -> onRetrievePostListError(throwable) }
                )
        }
    }

    private fun onRetrievePostListStart(){
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
        buttonEnable.value = true
    }

    private fun onRetrievePostListSuccess(data: ImageData){
        imgListData.addAll(data.value)
        imgListAdapter.updateSearchList(imgListData)
    }

    private fun onRetrievePostListError(it: Throwable) {
        errorMessage.value = R.string.error
        Log.e("error_message",it.message.toString())
    }

    fun onSearchClicked(view: View){
        loadingVisibility.value = View.VISIBLE
        buttonEnable.value = false
        content = searchContent.value!!
    }

    fun addTextChanged(tv: EditText){
         d1 = RxTextView.textChanges(tv).subscribe{
            when{
                it.toString().length>2 -> {
                    buttonEnable.value = true
                    searchContent.value = it.toString()
                }
                else -> buttonEnable.value = false
            }
        }

    }

}
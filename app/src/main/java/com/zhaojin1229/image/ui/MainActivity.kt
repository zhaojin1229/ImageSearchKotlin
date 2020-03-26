package com.zhaojin1229.image.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.zhaojin1229.image.R
import com.zhaojin1229.image.dagger.ViewModelFactory
import com.zhaojin1229.image.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchListVM
    private var errorSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(SearchListVM::class.java)
        viewModel.errorMessage.observe(this, Observer {
                errorMessage -> if(errorMessage != null) showError(errorMessage) else hideError()
        })
        viewModel.loadingVisibility.observe(this, Observer {
                visibility -> if(visibility != null) showLoading(visibility) else hideLoading()
        })

        viewModel.page.observe(this, Observer {
                page -> if(page > 5) binding.listRes.setNoMore(true)
        })

        binding.viewModel = viewModel


        binding.listRes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.listRes.setRefreshProgressStyle(ProgressStyle.SysProgress)
        binding.listRes.setLoadingMoreProgressStyle(ProgressStyle.SysProgress)
        binding.listRes.setLoadingMoreEnabled(true)
        binding.listRes.setLoadingListener(viewModel.listLoadListener)


        viewModel.addTextChanged(binding.searchInput)
    }

    private fun showError(@StringRes errorMessage:Int){
        errorSnackBar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackBar?.show()
    }

    private fun hideError(){
        errorSnackBar?.dismiss()
    }

    private fun showLoading(v:Int?){
        when (v) {
            View.VISIBLE -> binding.listRes.refresh()
            else -> hideLoading()
        }
    }

    private fun hideLoading(){
        binding.listRes.refreshComplete()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        if (v != null &&
            (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            v is EditText &&
            !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.getLeft() - scrcoords[0]
            val y = ev.rawY + v.getTop() - scrcoords[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) hideKeyboardAndRemoveFocus(
                this,
                v
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboardAndRemoveFocus(activity: Activity?, v: View) {
        if (activity != null && activity.window != null && activity.window
                .decorView != null
        ) {
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
            v.clearFocus()
            v.isFocusable = false
            v.isFocusableInTouchMode = true
        }
    }

}

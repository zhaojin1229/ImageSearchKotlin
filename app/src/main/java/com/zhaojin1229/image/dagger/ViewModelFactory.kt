package com.zhaojin1229.image.dagger

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhaojin1229.image.ui.SearchListVM

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchListVM::class.java)) {
            return SearchListVM() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
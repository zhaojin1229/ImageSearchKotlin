package com.zhaojin1229.image.base

import androidx.lifecycle.ViewModel
import com.zhaojin1229.image.dagger.component.DaggerViewModelInjector
import com.zhaojin1229.image.dagger.component.ViewModelInjector
import com.zhaojin1229.image.dagger.module.NetworkModule
import com.zhaojin1229.image.ui.SearchItemVM
import com.zhaojin1229.image.ui.SearchListVM

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is SearchListVM -> injector.inject(this)
            is SearchItemVM -> injector.inject(this)
        }
    }
}
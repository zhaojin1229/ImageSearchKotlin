package com.zhaojin1229.image.dagger.component

import com.zhaojin1229.image.dagger.module.NetworkModule
import com.zhaojin1229.image.ui.SearchItemVM
import com.zhaojin1229.image.ui.SearchListVM
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    fun inject(searchListVM: SearchListVM)

    fun inject(searchListVM: SearchItemVM)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}
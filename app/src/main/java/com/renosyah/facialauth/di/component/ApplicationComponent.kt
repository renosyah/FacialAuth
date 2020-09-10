package com.renosyah.facialauth.di.component

import com.renosyah.facialauth.BaseApp
import com.renosyah.facialauth.di.module.ApplicationModule
import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: BaseApp)
}
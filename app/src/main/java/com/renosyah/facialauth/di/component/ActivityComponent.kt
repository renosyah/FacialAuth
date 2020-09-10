package com.renosyah.facialauth.di.component
import com.renosyah.facialauth.di.module.ActivityModule
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    // add for each new activity
}
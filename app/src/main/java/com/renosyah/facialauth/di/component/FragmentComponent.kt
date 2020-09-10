package com.renosyah.facialauth.di.component

import com.renosyah.facialauth.di.module.FragmentModule
import dagger.Component

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    // add for each new fragment
}
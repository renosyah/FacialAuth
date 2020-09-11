package com.renosyah.facialauth.ui.activity.voiceLogin

import com.renosyah.facialauth.base.BaseContract
import com.renosyah.facialauth.model.Student

class VoiceLoginActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onGetOneStudent(s : Student)
        fun showProgressOnGetOneStudent(show: Boolean)
        fun showErrorOnGetOneStudent(error: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getOneStudent(nim : String,enableLoading : Boolean)
    }
}
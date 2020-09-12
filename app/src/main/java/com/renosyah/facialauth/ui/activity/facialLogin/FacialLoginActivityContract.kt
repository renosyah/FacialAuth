package com.renosyah.facialauth.ui.activity.facialLogin

import com.renosyah.facialauth.base.BaseContract
import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.model.ValidateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FacialLoginActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onValidateImageProfile(v : ValidateResponse)
        fun showProgressValidateImageProfile(show: Boolean)
        fun showErrorValidateImageProfile(error: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun validateImageProfile(id : String, file: MultipartBody.Part, enableLoading : Boolean)
    }
}
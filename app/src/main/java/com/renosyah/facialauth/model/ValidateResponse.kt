package com.renosyah.facialauth.model

import com.google.gson.annotations.SerializedName

class ValidateResponse :BaseModel {

    @SerializedName("validate")
    var Validate : Boolean = false

    @SerializedName("message")
    var Message :String = ""

    @SerializedName("session_id")
    var SessionId : String = ""
}
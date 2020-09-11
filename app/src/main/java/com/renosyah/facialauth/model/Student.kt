package com.renosyah.facialauth.model

import com.google.gson.annotations.SerializedName

class Student : BaseModel {

    @SerializedName("id")
    var Id : String = ""

    @SerializedName("nim")
    var Nim : String = ""

    @SerializedName("name")
    var Name : String = ""

    @SerializedName("status")
    var Status : Int  = 0

    constructor(Nim: String, Name: String, Status: Int) {
        this.Nim = Nim
        this.Name = Name
        this.Status = Status
    }
}
package com.madan_patil.covidvaccination

data class CenterRVModel (
        val centerName: String,
        val centerAddress:String,
        val centerFromTime: String,
        val centerToTime: String,
        val feeType: String,
        val ageLimit: Int,
        val vaccineName: String,
        val availableCapacity: Int
        )
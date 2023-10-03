package com.screentimex.nouzze.Services

import com.screentimex.nouzze.models.TimeUsageData
import com.screentimex.nouzze.models.UserDetails

class PointsCalculation(private val userDetails: UserDetails, private val timeDetails: TimeUsageData) {
    private val previousPoints = userDetails.points
    private val timeList = timeDetails.timeList
    private val age = userDetails.age
    private val profession = userDetails.profession

    fun calculation() {
        if(age in 10..14) {

        }
    }
}
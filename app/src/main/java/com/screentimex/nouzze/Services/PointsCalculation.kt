package com.screentimex.nouzze.Services

import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TimeUsageData
import com.screentimex.nouzze.models.UserDetails

class PointsCalculation(private val userDetails: UserDetails, private val timeDetails: TimeUsageData) {
    private val previousPoints = userDetails.points
    private val timeList = timeDetails.timeList
    private val age = userDetails.age
    private val profession = userDetails.profession
    private val ApplicationTypes = ConstPoints.MAP_APP_TYPE

    fun ageBracket(): String {
        if(age in 10..14) return "10-14"
        if(age in 15..22) return "15-22"
        if(age in 23..35) return "23-35"
        if(age in 36..60) return "36-60"
        return "60+"
    }

    private fun pointsCalculation() {
        val specificAgeBracket = ageBracket(age)
        val mapProfessionLimit = ConstPoints.MAP_AGE_PROFESSION_TIME.get(specificAgeBracket)
        val mapsOfPointsData = ConstPoints.MAP_EVERYTHING.get(specificAgeBracket)

        for ( applicationssData in timeList ){
            val applicationName = applicationsData.appName
            val applicationTime = applicationsData.appStartTime

            if ( applicationTime  )
        }

    }
}
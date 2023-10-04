package com.screentimex.nouzze.models

data class TotalData(
    val userData: UserDetails = UserDetails(),
    val timeData: TimeUsageData = TimeUsageData()
)

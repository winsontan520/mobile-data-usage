package com.winsontan520.mobiledatausage.data.model

data class MobileDataUsage(
    var year: Int,
    var volumeOfMobileDataInYear: Double = 0.0,
    var recordQ1: Record? = null,
    var recordQ2: Record? = null,
    var recordQ3: Record? = null,
    var recordQ4: Record? = null,
    var isDecline: Boolean = false
)
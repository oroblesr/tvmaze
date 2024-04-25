package com.example.tvmaze.model

data class TvShow(
    val id: String? = null,
    val url: String? = null,
    val name: String? = null,
    val summary: String? = null,
    val network: TvNetwork? = null,
    val schedule: ShowSchedule? = null,
    val image: ShowImage? = null,
    val status: String? = null,
    val type: String? = null
)

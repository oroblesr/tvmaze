package com.example.tvmaze.model

data class TvMazeScheduleResponse(
    val id: String?,
    val url: String?,
    val name: String?,
    val summary: String?,
    val show: TvShow?
)

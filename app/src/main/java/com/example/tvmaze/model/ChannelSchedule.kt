package com.example.tvmaze.model

data class ChannelSchedule(
    val tvNetwork: TvNetwork,
    val scheduledShows: List<TvShow>
)

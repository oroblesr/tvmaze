package com.example.tvmaze.repository

import com.example.tvmaze.model.TvMazeScheduleResponse
import retrofit2.Response

interface ShowRepository {
    suspend fun getSchedule(): Response<List<TvMazeScheduleResponse?>?>?
}
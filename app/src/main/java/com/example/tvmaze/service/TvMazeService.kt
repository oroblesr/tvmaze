package com.example.tvmaze.service

import com.example.tvmaze.model.TvMazeScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface TvMazeService {
    @GET("schedule")
    suspend fun getSchedule(@QueryMap options: Map<String, String>): Response<List<TvMazeScheduleResponse?>?>?
}

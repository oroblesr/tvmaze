package com.example.tvmaze.repository

import com.example.tvmaze.model.TvMazeScheduleResponse
import com.example.tvmaze.service.TvMazeService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ShowRepository {

    override suspend fun getSchedule(): Response<List<TvMazeScheduleResponse?>?>? {
        return withContext(ioDispatcher) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.tvmaze.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(TvMazeService::class.java).getSchedule(mapOf("country" to "US"))
        }
    }
}

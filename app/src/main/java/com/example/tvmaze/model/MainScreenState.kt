package com.example.tvmaze.model

data class MainScreenState(
    val channelSchedule: List<ChannelSchedule> = listOf(),
) {
    companion object {
        fun from(scheduleList: List<TvMazeScheduleResponse?>): MainScreenState {
            if (scheduleList.isEmpty()) {
                return MainScreenState()
            }
            val channelList = mutableSetOf<TvNetwork>()
            val channelShowListMap = mutableMapOf<String, List<TvShow>>()
            for (schedule in scheduleList) {
                if (schedule?.show?.network?.id != null) {
                    channelList.add(schedule.show.network)

                    // Map channel id to list of shows
                    if (!channelShowListMap.containsKey(schedule.show.network.id)) {
                        channelShowListMap[schedule.show.network.id] = listOf(schedule.show)
                    } else {
                        val list = channelShowListMap[schedule.show.network.id]?.toMutableList()
                        list?.add(schedule.show)
                        channelShowListMap[schedule.show.network.id] = list?.toList().orEmpty()
                    }
                }
            }
            val sortedChannelList = channelList.sortedBy { it.name }
            val channelSchedule = mutableListOf<ChannelSchedule>()

            for (channel in sortedChannelList) {
                val showList = channelShowListMap[channel.id]
                // Add shows to channel and sort them by airTime
                channelSchedule.add(ChannelSchedule(channel, showList?.sortedBy { it.schedule?.time }.orEmpty()))
            }

            return MainScreenState(channelSchedule = channelSchedule.toList())
        }
    }
}

package com.vhkfoundation.model

data class GetEventCatigoryModel(
    val `data`: List<EventCatData>,
    val message: String,
    val status: Int
)


data class EventCatData(
    val created_at: String,
    val event_count: Int,
    val id: Int,
    val name: String,
    val slug: String,
    val status: String,
    val updated_at: String
)
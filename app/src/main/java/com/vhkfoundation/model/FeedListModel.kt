package com.vhkfoundation.model

class FeedListModel : ArrayList<FeedListModelItem>()

data class FeedListModelItem(
    val datetime: String,
    val description: Any,
    val feed_created_by: String,
    val id: String,
    val images: String,
    val is_donate: String,
    val is_emergency: String,
    val is_like: String,
    val title: String,
    val total_like: String
)
package com.vhkfoundation.model

class StateListModel : ArrayList<StateListModelItem>()

data class StateListModelItem(
    val id: Int,
    val name: String
)
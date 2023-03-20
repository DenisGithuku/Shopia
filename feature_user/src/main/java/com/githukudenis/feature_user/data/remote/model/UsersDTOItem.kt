package com.githukudenis.feature_user.data.remote.model

data class UsersDTOItem(
    val __v: Int,
    val address: Address,
    val email: String,
    val id: Int,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
)
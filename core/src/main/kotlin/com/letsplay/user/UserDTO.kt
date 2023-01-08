package com.letsplay.user

data class UserDTO(
    var id: String,
    var name: String?,
    var avatarUrl: String? = null,
    var data: Map<String, String> = mutableMapOf()
)

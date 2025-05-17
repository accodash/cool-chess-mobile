package pl.accodash.coolchess.api.models

import com.google.gson.annotations.SerializedName

data class User(
    val uuid: String,
    val username: String,
    val createdAt: String,
    val imageUrl: String?,
    val followersCount: Int? = null,
    val ratings: List<Rating>? = null,
    @SerializedName("followed_users") val followedUsers: List<String>? = null,
    val followers: List<String>? = null
)
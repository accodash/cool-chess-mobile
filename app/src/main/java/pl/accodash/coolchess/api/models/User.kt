package pl.accodash.coolchess.api.models

import com.google.gson.annotations.SerializedName
import pl.accodash.coolchess.BuildConfig

data class User(
    val uuid: String,
    val username: String,
    val createdAt: String,
    val imageUrl: String?,
    val followersCount: Int? = null,
    val ratings: List<Rating>? = null,
    @SerializedName("followed_users") val followedUsers: List<User>? = null,
    val followers: List<User>? = null
) {
    fun getBackendImageUrl(): String? =
        imageUrl?.replace(
            BuildConfig.IMAGE_URLS_PREFIX,
            BuildConfig.BACKEND_URL.removeSuffix("/").replace(Regex(":\\d+"), "")
        )
}

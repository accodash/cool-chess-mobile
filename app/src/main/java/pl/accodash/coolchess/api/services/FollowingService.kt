package pl.accodash.coolchess.api.services

import pl.accodash.coolchess.api.models.Following
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowingService {
    @GET("following/followers/{userId}")
    suspend fun fetchFollowers(@Path("userId") userId: String): List<Following>

    @GET("following/followings/{userId}")
    suspend fun fetchFollowings(@Path("userId") userId: String): List<Following>

    @POST("following/followers/{targetUserId}")
    suspend fun followUser(@Path("targetUserId") targetUserId: String)

    @DELETE("following/followers/{targetUserId}")
    suspend fun unfollowUser(@Path("targetUserId") targetUserId: String): Response<Unit>
}

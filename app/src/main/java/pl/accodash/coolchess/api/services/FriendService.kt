package pl.accodash.coolchess.api.services

import pl.accodash.coolchess.api.models.FriendRelation
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendService {
    @GET("friends/current-user")
    suspend fun fetchFriends(): List<FriendRelation>

    @GET("friends/current-user/received")
    suspend fun fetchReceivedRequests(): List<FriendRelation>

    @GET("friends/current-user/sent")
    suspend fun fetchSentRequests(): List<FriendRelation>

    @POST("friends/sent/{userId}")
    suspend fun sendFriendRequest(@Path("userId") userId: String): FriendRelation

    @POST("friends/accept/{requestId}")
    suspend fun acceptFriendRequest(@Path("requestId") requestId: String): FriendRelation

    @POST("friends/remove/{requestId}")
    suspend fun removeFriend(@Path("requestId") requestId: String)
}


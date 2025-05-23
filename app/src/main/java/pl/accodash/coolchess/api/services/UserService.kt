package pl.accodash.coolchess.api.services

import okhttp3.MultipartBody
import pl.accodash.coolchess.api.models.User
import retrofit2.http.*

interface UserService {
    @GET("user")
    suspend fun fetchUsers(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("sortBy") sortBy: String,
        @Query("order") order: String,
        @Query("search") search: String? = null
    ): List<User>

    @GET("user/current-user")
    suspend fun getCurrentUser(): User

    @GET("user/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @PUT("user/current-user")
    suspend fun updateCurrentUser(@Body data: UpdateUserRequest): User

    @Multipart
    @POST("user/current-user/upload/avatar")
    suspend fun uploadUserAvatar(@Part avatar: MultipartBody.Part): UploadAvatarResponse
}

data class UpdateUserRequest(
    val username: String?,
    val imageUrl: String?
)

data class UploadAvatarResponse(
    val imageUrl: String
)

package pl.accodash.coolchess.api.services

import pl.accodash.coolchess.api.models.User
import retrofit2.http.GET

interface UserService {
    @GET("user/current-user")
    suspend fun getCurrentUser(): User
}

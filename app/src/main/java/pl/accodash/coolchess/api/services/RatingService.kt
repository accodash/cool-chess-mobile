package pl.accodash.coolchess.api.services

import pl.accodash.coolchess.api.models.Rating
import retrofit2.http.GET
import retrofit2.http.Query

interface RatingService {
    @GET("rating/ranking")
    suspend fun fetchRanking(
        @Query("mode") mode: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<Rating>
}

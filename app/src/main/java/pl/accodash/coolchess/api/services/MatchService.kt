package pl.accodash.coolchess.api.services

import pl.accodash.coolchess.api.models.Match
import pl.accodash.coolchess.api.models.MovesBySquare
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MatchService {
    @GET("match/list")
    suspend fun fetchMatches(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<Match>

    @GET("match/{id}")
    suspend fun fetchMatch(
        @Path("id") matchId: String
    ): Match

    @GET("match/{id}/get-moves")
    suspend fun fetchPossibleMovesForMatch(
        @Path("id") matchId: String
    ): MovesBySquare
}

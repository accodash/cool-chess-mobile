package pl.accodash.coolchess.api.services

import pl.accodash.coolchess.api.models.Move
import retrofit2.http.GET
import retrofit2.http.Path

interface MoveService {
    @GET("move/list/{matchId}")
    suspend fun fetchMoves(
        @Path("matchId") matchId: String
    ): List<Move>
}

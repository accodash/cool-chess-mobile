package pl.accodash.coolchess.api

import pl.accodash.coolchess.api.services.*

data class CoolChessServices(
    val userService: UserService,
    val friendService: FriendService,
    val followingService: FollowingService,
    val ratingService: RatingService,
    val matchService: MatchService,
    val moveService: MoveService
)

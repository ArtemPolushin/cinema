package domain

import kotlinx.serialization.Serializable

@Serializable
data class SeatEntity(
    var number: Int,
    var status: SeatStatus = SeatStatus.FREE
)
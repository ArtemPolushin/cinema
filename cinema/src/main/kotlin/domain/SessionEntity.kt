package domain

import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class SessionEntity(
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    var startTime : LocalDateTime,
    var filmId: Int,
    var seats: MutableList<SeatEntity> = mutableListOf()
)
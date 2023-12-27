package domain
import kotlinx.serialization.Serializable
@Serializable
data class TicketEntity(
    val sessionId: Int,
    val seatNumber: Int
)

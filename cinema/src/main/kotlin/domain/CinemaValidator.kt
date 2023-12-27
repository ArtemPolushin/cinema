package domain

import data.TicketDao
import data.CinemaDao
import kotlinx.datetime.LocalDateTime
import presentation.OutputModel

sealed class Result

object Success : Result()
class Error(val outputModel: OutputModel) : Result()


class CinemaValidator(
    private val ticketDao: TicketDao,
    private val cinemaDao: CinemaDao
) {

    fun validateSession(sessionId: Int): Result {
        return if (sessionId >= 0 && sessionId < cinemaDao.getAllSessions().size) {
            Success
        } else {
            Error(OutputModel("Session with ID $sessionId doesn't exist"))
        }
    }

    fun validateFilm(filmId: Int): Result {
        return if (filmId >= 0 && filmId < cinemaDao.getAllFilms().size) {
            Success
        } else {
            Error(OutputModel("Film with ID $filmId doesn't exist"))
        }
    }

    fun validateSeat(seatNumber: Int): Result {
        return if (seatNumber >= 0 && seatNumber < cinemaDao.cinemaCapacity) {
            Success
        } else {
            Error(OutputModel("Seat with number $seatNumber doesn't exist"))
        }
    }
    fun validateAddSession(startTime: LocalDateTime, filmId: Int): Result {
        val result = validateFilm(filmId)
        if (result is Error) {
            return result
        }
        cinemaDao.getAllSessions().forEach {
            if (it.startTime == startTime) {
                return Error(OutputModel("A session with this started time already exists"))
            }
        }
        return Success
    }

    fun validateChangeSessionTime(sessionId: Int, newStartTime: LocalDateTime): Result {
        val result = validateSession(sessionId)
        if (result is Error) {
            return result
        }
        cinemaDao.getAllSessions().forEach {
            if (it.startTime == newStartTime) {
                return Error(OutputModel("A session with this start time already exists"))
            }
        }
        return Success
    }

    fun validateChangeSessionFilm(sessionId: Int, newFilmId: Int): Result {
        val result = validateSession(sessionId)
        if (result is Error) {
            return result
        }
        return validateFilm(newFilmId)
    }

    fun validateBuyTicket(sessionId: Int, seatNumber: Int): Result {
        var result = validateSession(sessionId)
        if (result is Error) {
            return result
        }
        result = validateSeat( seatNumber)
        if (result is Error) {
            return result
        }
        val session = cinemaDao.getSession(sessionId)
        if (session.seats[seatNumber].status != SeatStatus.FREE) {
            return Error(OutputModel("The seat is already taken"))
        }
        return Success
    }
}
package domain

import data.CinemaDao
import data.TicketDao
import data.TicketDaoImpl
import kotlinx.datetime.LocalDateTime
import presentation.OutputModel

interface FilmController {
    fun addFilm(name: String, information: String): OutputModel
    fun getAllFilms(): OutputModel
}

interface SessionController {
    fun addSession(startTime: LocalDateTime, filmId: Int): OutputModel
    fun getAllSessions(): OutputModel

    fun showSeats(sessionId: Int): OutputModel
    fun changeSessionTime(sessionId: Int, newStartTime: LocalDateTime) : OutputModel
    fun changeSessionFilm(sessionId: Int, newFilmId: Int) : OutputModel

}

interface TicketController {
    fun buyTicket(sessionId: Int, seatNumber: Int) : OutputModel

    fun returnTicket(ticketId: Int): OutputModel

    fun passTicket(ticketId: Int): OutputModel
}

class CinemaControllerImpl(
    private val ticketDao: TicketDao = TicketDaoImpl(),
    private val cinemaDao: CinemaDao = CinemaDao(),
    private val cinemaValidator: CinemaValidator = CinemaValidator(ticketDao, cinemaDao)
)  : FilmController, SessionController, TicketController {
    override fun addFilm(name: String, information: String): OutputModel {
        val filmId = cinemaDao.addFilm(name, information)
        return OutputModel("Film added. Its ID is $filmId")
    }
    override fun getAllFilms(): OutputModel {
        val films = cinemaDao.getAllFilms().joinToString()
        return OutputModel(films).takeIf { it.message.isNotEmpty() }
            ?: OutputModel("No films added")
    }

    override fun addSession(startTime: LocalDateTime, filmId: Int): OutputModel {
        return when(val result = cinemaValidator.validateAddSession(startTime, filmId)) {
            is Error -> result.outputModel
            Success -> {
                val sessionId = cinemaDao.addSession(startTime, filmId)
                return OutputModel("Session added. Its ID is $sessionId")
            }
        }

    }

    override fun getAllSessions(): OutputModel {
        val sessions = cinemaDao.getAllFilms().joinToString()
        return OutputModel(sessions).takeIf { it.message.isNotEmpty() }
            ?: OutputModel("No sessions added")
    }

    override fun showSeats(sessionId: Int): OutputModel {
        val result = cinemaValidator.validateSession(sessionId)
        if (result is Error) {
            return result.outputModel
        }
        val session = cinemaDao.getSession(sessionId)
        return OutputModel(session.seats.joinToString())
    }

    override fun changeSessionTime(sessionId: Int, newStartTime: LocalDateTime): OutputModel {
        val result = cinemaValidator.validateChangeSessionTime(sessionId, newStartTime)
        if (result is Error) {
            return result.outputModel
        }
        val newSessionEntity = cinemaDao.getSession(sessionId).copy(startTime = newStartTime)
        cinemaDao.changeSession(sessionId, newSessionEntity)
        return OutputModel("Session start time has been changed")
    }

    override fun changeSessionFilm(sessionId: Int, newFilmId: Int): OutputModel {
        val result = cinemaValidator.validateChangeSessionFilm(sessionId, newFilmId)
        if (result is Error) {
            return result.outputModel
        }
        val newSessionEntity = cinemaDao.getSession(sessionId).copy(filmId = newFilmId)
        cinemaDao.changeSession(sessionId, newSessionEntity)
        return OutputModel("Session film has been changed")
    }

    override fun buyTicket(sessionId: Int, seatNumber: Int): OutputModel {
        val result = cinemaValidator.validateBuyTicket(sessionId, seatNumber)
        if (result is Error) {
            return result.outputModel
        }
        val session = cinemaDao.getSession(sessionId)
        session.seats[seatNumber].status = SeatStatus.RESERVED
        cinemaDao.changeSession(sessionId, session)
        val ticketId = ticketDao.addTicket(TicketEntity(sessionId, seatNumber))
        return OutputModel("The ticket is purchased. Its ID is $ticketId")
    }

    override fun returnTicket(ticketId: Int): OutputModel {
        if (!ticketDao.getAllTickets().containsKey(ticketId)) {
            return OutputModel("Ticket doesn't exist")
        }
        val ticket = ticketDao.getTicket(ticketId) ?: return OutputModel("Ticket doesn't exist")
        val session = cinemaDao.getSession(ticket.sessionId)
        if (session.seats[ticket.seatNumber].status != SeatStatus.RESERVED) {
            return OutputModel("The seat is not reserved")
        }
        session.seats[ticket.seatNumber].status = SeatStatus.FREE
        ticketDao.removeTicket(ticketId)
        return OutputModel("The ticket has been removed")
    }

    override fun passTicket(ticketId: Int): OutputModel {
        if (!ticketDao.getAllTickets().containsKey(ticketId)) {
            return OutputModel("Ticket doesn't exist")
        }
        val ticket = ticketDao.getTicket(ticketId) ?: return OutputModel("Ticket doesn't exist")
        val session = cinemaDao.getSession(ticket.sessionId)
        if (session.seats[ticket.seatNumber].status != SeatStatus.RESERVED) {
            return OutputModel("The seat is not reserved")
        }
        session.seats[ticket.seatNumber].status = SeatStatus.BUSY
        return OutputModel("The ticket is valid")
    }
}
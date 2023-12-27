package data

import domain.FilmEntity
import domain.SeatEntity
import domain.SeatStatus
import domain.SessionEntity
import kotlinx.datetime.LocalDateTime

interface FilmsDao {
    var films: MutableList<FilmEntity>
    fun addFilm(name: String, information: String): Int
    fun getAllFilms(): MutableList<FilmEntity>
    fun getFilm(id: Int): FilmEntity
}

interface SessionsDao {
    var sessions: MutableList<SessionEntity>
    fun addSession(startTime: LocalDateTime, filmId: Int): Int
    fun getAllSessions(): MutableList<SessionEntity>
    fun getSession(id: Int): SessionEntity
    fun changeSession(id: Int, newValue: SessionEntity)

    fun getSeatStatus(id: Int, seatNumber: Int): SeatStatus
    fun changeSeatStatus(id: Int, seatNumber: Int, seatStatus: SeatStatus)
}

class CinemaDao(
    val cinemaCapacity: Int = 100
) : FilmsDao, SessionsDao {
    override var films: MutableList<FilmEntity> = mutableListOf()
    override var sessions: MutableList<SessionEntity> = mutableListOf()
    override fun addFilm(name: String, information: String): Int {
        val film = FilmEntity(name, information)
        films.add(film)
        return films.size - 1
    }
    override fun getAllFilms(): MutableList<FilmEntity> {
        return films
    }
    override fun getFilm(id: Int): FilmEntity {
        return films[id]
    }

    override fun addSession(startTime: LocalDateTime, filmId: Int): Int {
        val session = SessionEntity(startTime, filmId)
        for (i in 0..<cinemaCapacity) {
            session.seats.add(SeatEntity(i))
        }
        sessions.add(session)
        return sessions.size - 1
    }
    override fun getAllSessions(): MutableList<SessionEntity> {
        return sessions
    }
    override fun getSession(id: Int): SessionEntity {
        return sessions[id]
    }

    override fun changeSession(id: Int, newValue: SessionEntity) {
        sessions[id] = newValue
    }

    override fun getSeatStatus(id: Int, seatNumber: Int): SeatStatus {
        return sessions[id].seats[seatNumber].status
    }

    override fun changeSeatStatus(id: Int, seatNumber: Int, seatStatus: SeatStatus) {
        sessions[id].seats[seatNumber].status = seatStatus
    }
}
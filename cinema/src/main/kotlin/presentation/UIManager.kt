package presentation

import domain.CinemaControllerImpl
import kotlinx.datetime.LocalDateTime

class UIManager {
    private val cinemaController = CinemaControllerImpl()
    fun start() {
        println("Possible commands:\n" +
                "Add film\n" +
                "Add session\n" +
                "Change session\n" +
                "Show seats\n" +
                "Buy ticket\n" +
                "Return ticket\n" +
                "Pass ticket\n" +
                "Exit")
        while (true) {
            val command = readln()
            if (command == "Exit") {
                break
            }
            if (command == "Add film") {
                print("Enter film name: ")
                val name = readln()
                print("Enter information about film: ")
                val information = readln()
                val output = cinemaController.addFilm(name, information)
                println(output.message)
                continue
            }
            if (command == "Add session") {
                try {
                    print("Enter year of session: ")
                    val year: Int = readln().toInt()
                    print("Enter month of session: ")
                    val month: Int = readln().toInt()
                    print("Enter day of session: ")
                    val day: Int = readln().toInt()
                    print("Enter hour of session: ")
                    val hour: Int = readln().toInt()
                    print("Enter minute of session: ")
                    val minute: Int = readln().toInt()
                    print("Enter ID of film: ")
                    val filmId: Int = readln().toInt()
                    val output = cinemaController.addSession(LocalDateTime(year, month, day, hour, minute), filmId)
                    println(output.message)
                } catch (e: Exception) {
                    println(e.message)
                }
                continue
            }
            if (command == "Change session") {
                try {
                    print("Enter ID of session: ")
                    val sessionId = readln().toInt()
                    print("Enter time or film: ")
                    val type = readln()
                    if (type == "time") {
                        print("Enter year of session: ")
                        val year: Int = readln().toInt()
                        print("Enter month of session: ")
                        val month: Int = readln().toInt()
                        print("Enter day of session: ")
                        val day: Int = readln().toInt()
                        print("Enter hour of session: ")
                        val hour: Int = readln().toInt()
                        print("Enter minute of session: ")
                        val minute: Int = readln().toInt()
                        val output =
                            cinemaController.changeSessionTime(sessionId, LocalDateTime(year, month, day, hour, minute))
                        println(output.message)
                    } else if (type == "film") {
                        print("Enter ID of film: ")
                        val filmId: Int = readln().toInt()
                        val output = cinemaController.changeSessionFilm(sessionId, filmId)
                        println(output.message)
                    } else {
                        println("Incorrect argument")
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
                continue
            }
            if (command == "Show seats") {
                try {
                    print("Enter ID of session: ")
                    val sessionId = readln().toInt()
                    val output = cinemaController.showSeats(sessionId)
                    println(output.message)
                } catch (e: Exception) {
                    println(e.message)
                }
                continue
            }
            if (command == "Buy ticket") {
                try {
                    print("Enter ID of session: ")
                    val sessionId = readln().toInt()
                    print("Enter number of seat: ")
                    val seatNumber = readln().toInt()
                    val output = cinemaController.buyTicket(sessionId, seatNumber)
                    println(output.message)
                } catch (e: Exception) {
                    println(e.message)
                }
                continue
            }
            if (command == "Return ticket") {
                try {
                    print("Enter ID of ticket: ")
                    val ticketId = readln().toInt()
                    val output = cinemaController.returnTicket(ticketId)
                    println(output.message)
                } catch (e: Exception) {
                    println(e.message)
                }
                continue
            }
            if (command == "Pass ticket") {
                try {
                    print("Enter ID of ticket: ")
                    val ticketId = readln().toInt()
                    val output = cinemaController.passTicket(ticketId)
                    println(output.message)
                } catch (e: Exception) {
                    println(e.message)
                }
                continue
            }
            println("Incorrect command")
        }
    }
}
package data

import domain.TicketEntity

interface TicketDao {
    fun getAllTickets(): MutableMap<Int, TicketEntity?>
    fun addTicket(ticket: TicketEntity): Int
    fun removeTicket(ticketId: Int)
    fun getTicket(ticketId: Int): TicketEntity?
}
class TicketDaoImpl(
    private var counter: Int = 0
) : TicketDao {
    private var tickets: MutableMap<Int, TicketEntity?> = mutableMapOf()
    override fun getAllTickets(): MutableMap<Int, TicketEntity?> {
        return tickets
    }

    override fun addTicket(ticket: TicketEntity): Int {
        tickets[counter] = ticket
        return counter++
    }

    override fun removeTicket(ticketId: Int) {
        tickets[ticketId] = null
    }

    override fun getTicket(ticketId: Int): TicketEntity? {
        return tickets[ticketId]
    }
}
package uk.co.jatra.noted.model

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT occurrence.id as id, " +
            "userId, " +
            "event.name as eventName, " +
            "event.description as description, " +
            "date, " +
            "time, " +
            "occurrence.notes as notes " +
            "FROM occurrence, event " +
            "WHERE occurrence.eventId = event.id"
)
data class OccurrenceDetail(
    val id: String,
    val userId: String,
    val eventName: String,
    val description: String,
    val date: String,
    val time: String,
    val notes: String
)
package uk.co.jatra.notedui.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An Occurrence.
 *
 * A description of something that has happened.
 * Each [Occurrence] in an instance of an [Event]
 * @property[id] The server assigned id of the occurrence.
 * @property[date] The date of the occurrence
 * @property[time] The time of day of the occurrence (in seconds of day) (or maybe hh:mm)
 * @property[userId] The user that created the occurrence
 * @property[eventId] the event the occurrence refers to
 * @property[detail] anything extra
 */
@Entity(tableName = "occurrence")
data class Occurrence(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "eventId") val eventId: String,
    @ColumnInfo(name = "notes") val detail: String
)

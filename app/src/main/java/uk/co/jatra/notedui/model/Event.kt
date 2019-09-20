package uk.co.jatra.notedui.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An Event.
 *
 * A description of something that can be recorded multiple times.
 * Each instance of an [Event] is an [Occurrence]
 * @property[id] The server assigned id of the event.
 * @property[name] The name of the event.
 * @property[description] The description of the event.
 */
@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    val name: String,
    val description: String
//    @ColumnInfo(name = "name") val name: String,
//    @ColumnInfo(name = "description") val description: String
)

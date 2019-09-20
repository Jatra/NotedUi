package uk.co.jatra.notedui.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

import io.reactivex.Flowable
import uk.co.jatra.notedui.model.Event

/**
 * Data Access Object for the events table.
 */
@Dao
interface EventDao {

    /**
     * Get a event by id.

     * @return the event from the table with a specific id.
     */
    @Query("SELECT * FROM event WHERE id = :id")
    fun getEventById(id: String): Flowable<Event>

    @Query("SELECT * FROM Event")

    fun getAllEvents(): Flowable<List<Event>>

    /**
     * Insert a event in the database. If the event already exists, replace it.

     * @param event the event to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event): Completable

    /**
     * Delete all events.
     */
    @Query("DELETE FROM Event")
    fun deleteAllEvents()
}
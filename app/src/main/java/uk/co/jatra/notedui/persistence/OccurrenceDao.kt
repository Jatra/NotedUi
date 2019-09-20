package uk.co.jatra.notedui.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

import io.reactivex.Flowable
import uk.co.jatra.noted.model.OccurrenceDetail
import uk.co.jatra.notedui.model.Occurrence

/**
 * Data Access Object for the occurrences table.
 */
@Dao
interface OccurrenceDao {

    /**
     * Get a occurrence by id.
     * @return the occurrence from the table with a specific id.
     */
    @Query("SELECT * FROM Occurrence WHERE id = :id")
    fun getOccurrenceById(id: String): Flowable<Occurrence>

    /**
     * Get all occurrences
     * @return all occurrences
     */

    @Query("SELECT * FROM Occurrence ORDER BY date,time DESC")
    fun getAllOccurrences(): Flowable<List<Occurrence>>

    /**
     * Get all occurrences of a specific date
     * @return all occurrences of [date]
     */

    @Query("SELECT * FROM Occurrence WHERE date = :date ORDER BY time DESC")
    fun getAllOccurrencesByDate(date: String): Flowable<List<Occurrence>>

    /**
     * Insert a occurrence in the database. If the occurrence already exists, replace it.
     * @param occurrence the occurrence to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOccurrence(occurrence: Occurrence): Completable

    /**
     * Delete all occurrences.
     */
    @Query("DELETE FROM Occurrence")
    fun deleteAllOccurrences()

    /**
     * Get [OccurrenceDetail] (join of Occurrence and Event) of a specific date
     * @return all Occurrences with details, for a specific date.
     */
    @Query("SELECT * FROM OccurrenceDetail WHERE date = :date ORDER BY time DESC")
    fun getAllOccurrencesDetailsByDate(date: String): Flowable<List<OccurrenceDetail>>
}
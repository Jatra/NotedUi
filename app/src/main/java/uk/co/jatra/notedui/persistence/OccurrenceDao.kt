package uk.co.jatra.notedui.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.co.jatra.notedui.model.Occurrence
import uk.co.jatra.notedui.model.OccurrenceDetail

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
    fun getOccurrenceById(id: String): Flow<Occurrence>

    /**
     * Get all occurrences
     * @return all occurrences
     */

    @Query("SELECT * FROM Occurrence ORDER BY date,time DESC")
    fun getAllOccurrences(): Flow<List<Occurrence>>

    /**
     * Get all occurrences of a specific date
     * @return all occurrences of [date]
     */

    @Query("SELECT * FROM Occurrence WHERE date = :date ORDER BY time DESC")
    fun getAllOccurrencesByDate(date: String): Flow<List<Occurrence>>

    /**
     * Insert a occurrence in the database. If the occurrence already exists, replace it.
     * @param occurrence the occurrence to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccurrence(occurrence: Occurrence)

    /**
     * Delete all occurrences.
     */
    @Query("DELETE FROM Occurrence")
    suspend fun deleteAllOccurrences()

    /**
     * Get [OccurrenceDetail] (join of Occurrence and Event) of a specific date
     * @return all Occurrences with details, for a specific date.
     */
    @Query("SELECT * FROM OccurrenceDetail WHERE date = :date ORDER BY time DESC")
    fun getAllOccurrencesDetailsByDate(date: String): Flow<List<OccurrenceDetail>>

    /**
     * Get all [OccurrenceDetail] (join of Occurrence and Event)
     * @return all Occurrences with details
     */
    @Query("SELECT * FROM OccurrenceDetail ORDER BY time DESC")
    fun getAllOccurrencesDetails(): Flow<List<OccurrenceDetail>>


    /**
     * Delete a specific occurrence.
     * @param id the id of the occurrence to delete
     */
    @Query("DELETE FROM occurrence WHERE id = :id")
    fun deleteOccurrence(id: String)
}
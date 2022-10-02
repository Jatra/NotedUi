package uk.co.jatra.notedui.repositories

import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import uk.co.jatra.notedui.model.Event
import uk.co.jatra.notedui.model.Occurrence
import uk.co.jatra.notedui.model.OccurrenceDetail
import uk.co.jatra.notedui.persistence.EventDao
import uk.co.jatra.notedui.persistence.OccurrenceDao
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val TAG = "OccurrenceRepository"

@Singleton
class OccurrenceRepository @Inject constructor(
    private val occurrenceDao: OccurrenceDao,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val eventDao: EventDao
) {

    init {
//        addDummyOccurrences()
    }

    val occurrenceDetailsSubject: BehaviorSubject<List<OccurrenceDetail>> = BehaviorSubject.create()
    val occurrenceDetailsState: MutableSharedFlow<List<OccurrenceDetail>> = MutableSharedFlow()

    suspend fun getDetailsByDay(date: LocalDate) {
        occurrenceDao.getAllOccurrencesDetailsByDate(date.toString())
            .collect {
                occurrenceDetailsState.emit(it)
            }
    }

//    suspend fun getDetailsByDay(date: LocalDate, responseSubject: Subject<List<OccurrenceDetail>>) {
//        occurrenceDao.getAllOccurrencesDetailsByDate(date.toString())
//            .subscribe {
//                responseSubject.onNext(it)
//            }
//    }

    suspend fun getAllDetails() {
        occurrenceDao.getAllOccurrencesDetails()
            .collect {
                Log.d("ARCHIVE", it.joinToString("\n"))
            }
    }

    suspend fun addOccurrence(eventId: String) {
        val allEvents: Flow<List<Event>> = eventDao.getAllEvents()
        allEvents.first()
            .first { event ->
                event.id == eventId.toLong()
            }
            .let { event ->
                occurrenceDao.insertOccurrence(makeOccurrence(event))
                getDetailsByDay(LocalDate.now())
            }
    }

    suspend fun removeOccurrence(id: String) {
        occurrenceDao.deleteOccurrence(id)
//            .subscribeOn(ioScheduler)
//            .subscribe {
        getDetailsByDay(LocalDate.now())
//            }
    }

    private fun makeOccurrence(event: Event) =
        Occurrence(
            id = 0,
            date = LocalDate.now().toString(),
            time = LocalTime.now().format(DateTimeFormatter.ofPattern("N")), //nano-of-day
            userId = "User1",
            eventId = event.id.toString(),
            detail = "${event.name}, ${event.description}"
        )

    private suspend fun addDummyOccurrences() {
        occurrenceDao.insertOccurrence(
            Occurrence(
                0,
                "2019-09-19",
                "00:00",
                "User1",
                "eventID",
                "detail 1"
            )
        )
        Log.d(TAG, "Loaded 1")
        occurrenceDao.insertOccurrence(
            Occurrence(
                0,
                "2019-09-19",
                "00:01",
                "User1",
                "eventID2",
                "detail 2"
            )
        )
        Log.d(TAG, "Loaded 2")
        occurrenceDao.insertOccurrence(
            Occurrence(
                0,
                "2019-09-19",
                "00:02",
                "User1",
                "eventID3",
                "detail 3"
            )
        )
        Log.d(TAG, "Loaded 3")
    }

}
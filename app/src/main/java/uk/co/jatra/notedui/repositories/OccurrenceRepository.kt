package uk.co.jatra.notedui.repositories

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
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


    fun getDetailsByDay(date: LocalDate) {
        occurrenceDao.getAllOccurrencesDetailsByDate(date.toString())
            .subscribe {
                occurrenceDetailsSubject.onNext(it)
            }
    }

    fun getDetailsByDay(date: LocalDate, responseSubject: Subject<List<OccurrenceDetail>>) {
        occurrenceDao.getAllOccurrencesDetailsByDate(date.toString())
            .subscribe {
                responseSubject.onNext(it)
            }
    }

    fun getAllDetails() {
        occurrenceDao.getAllOccurrencesDetails()
            .subscribe {
                Log.d("ARCHIVE", it.joinToString("\n"))
            }
    }

    fun addOccurrence(eventId: String) {
        eventDao.getAllEvents()
            .flatMap { list ->
                Flowable.fromIterable(list)
                    .filter { event -> event.id == eventId.toLong() }
            }
            .subscribe { event ->
                occurrenceDao.insertOccurrence(makeOccurrence(event))
                    .subscribeOn(ioScheduler)
                    .subscribe {
                        getDetailsByDay(LocalDate.now())
                    }

            }
    }

    fun removeOccurrence(id: String) {
        occurrenceDao.deleteOccurrence(id)
            .subscribeOn(ioScheduler)
            .subscribe {
                getDetailsByDay(LocalDate.now())
            }
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

    private fun addDummyOccurrences() {
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
            .subscribeOn(ioScheduler)
            .subscribe {
                Log.d(TAG, "Loaded 1")
            }
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
            .subscribeOn(ioScheduler)
            .subscribe {
                Log.d(TAG, "Loaded 2")
            }
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
            .subscribeOn(ioScheduler)
            .subscribe {
                Log.d(TAG, "Loaded 3")
            }
    }

}
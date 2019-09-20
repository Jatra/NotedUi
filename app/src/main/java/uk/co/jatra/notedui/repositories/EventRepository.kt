package uk.co.jatra.notedui.repositories

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import uk.co.jatra.notedui.model.Event
import uk.co.jatra.notedui.persistence.EventDao
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "EventRepository"

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {

    init {
        eventDao.getAllEvents()
            .subscribe { list ->
                if (list.isEmpty()) {
                    insertBasicEvents()
                }
            }
    }

    private fun insertBasicEvents() {
        eventDao.insertEvent(Event(0, "Paracetamol", "2 x 500mg"))
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG, "Loaded e1")
                eventDao.insertEvent(Event(0, "Ibruprofen", "2 x 200mg"))
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        Log.d(TAG, "Loaded e2")
                        eventDao.insertEvent(Event(0, "Cocodamol", "2 x 8/500mg"))
                            .subscribeOn(Schedulers.io())
                            .subscribe {
                                Log.d(TAG, "Loaded e3")
                            }
                    }
            }
    }

    val subject: BehaviorSubject<List<Event>> = BehaviorSubject.create()

    fun getData() {
        val allEvents: Flowable<List<Event>> = eventDao.getAllEvents()
        allEvents.subscribe {
            subject.onNext(it)
        }
    }
}
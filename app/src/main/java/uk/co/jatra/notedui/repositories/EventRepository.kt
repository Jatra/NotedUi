package uk.co.jatra.notedui.repositories

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import uk.co.jatra.notedui.model.Event
import uk.co.jatra.notedui.persistence.EventDao
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "EventRepository"

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {

    suspend fun insertBasicEvents() {
        eventDao.insertEvent(Event(0, "Paracetamol", "2 x 500mg"))
        Log.d(TAG, "Loaded e1")
        eventDao.insertEvent(Event(0, "Ibruprofen", "2 x 200mg"))
        Log.d(TAG, "Loaded e2")
        eventDao.insertEvent(Event(0, "Cocodamol", "2 x 8/500mg"))
        Log.d(TAG, "Loaded e3")
    }

    //    val subject: BehaviorSubject<List<Event>> = BehaviorSubject.create()
    private val _stateFlow: MutableSharedFlow<List<Event>> = MutableSharedFlow()
    val stateFlow: SharedFlow<List<Event>>
        get() = _stateFlow

    suspend fun getData() {
        eventDao.getAllEvents()
            .collect { eventList ->
                _stateFlow.emit(eventList)
            }
    }

    suspend fun getAllEvents(): List<Event> {
        return eventDao.getEventList()
    }
}
package uk.co.jatra.notedui.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import uk.co.jatra.notedui.repositories.OccurrenceRepository
import uk.co.jatra.notedui.ui.EventRequestListener
import uk.co.jatra.notedui.ui.OccurrenceRequestListener
import uk.co.jatra.notedui.ui.OccurrenceViewState

class OccurrenceViewModel(private val repository: OccurrenceRepository) : ViewModel(),
    EventRequestListener, OccurrenceRequestListener {
    val viewStates: MutableLiveData<List<OccurrenceViewState>> = MutableLiveData()

    init {
//        repository.occurrencesSubject.subscribe {
//            viewStates.postValue(it.map { occurrence ->O
//                OccurrenceViewState(
//                    "${occurrence.detail}",
//                    "${occurrence.detail}",
//                    "${occurrence.userId}",
//                    "at ${occurrence.time}, ${occurrence.date}"
//                )
//            })
//        }

        repository.occurrenceDetailsSubject.subscribe {
            viewStates.postValue(it.map { details ->
                OccurrenceViewState(
                    details.id,
                    details.eventName,
                    details.description,
                    details.userId,
                    "at ${details.time}"
                )
            })
        }

    }

    fun getDetails() {
        repository.getDetailsByDay(LocalDate.now())
    }

    fun getData() {
        repository.getDayData(LocalDate.now())
    }

    override fun addOccurrenceOfEvent(id: String) {
        repository.addOccurrence(id)
    }

    override fun removeOccurrence(id: String) {
        repository.removeOccurrence(id)
    }
}

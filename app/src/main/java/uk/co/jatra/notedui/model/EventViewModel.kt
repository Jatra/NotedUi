package uk.co.jatra.notedui.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.jatra.notedui.repositories.EventRepository
import uk.co.jatra.notedui.ui.EventViewState

class EventViewModel(private val repository: EventRepository) : ViewModel() {
    val viewStates: MutableLiveData<List<EventViewState>> = MutableLiveData()

    init {
        repository.subject.subscribe {
            viewStates.postValue(it.map { event ->
                EventViewState(event.id.toString(), event.name, event.description)
            })
        }
    }

    fun getData() {
        repository.getData()
    }
}

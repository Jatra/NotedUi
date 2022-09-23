package uk.co.jatra.notedui.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uk.co.jatra.notedui.repositories.EventRepository
import uk.co.jatra.notedui.ui.EventViewState
import javax.inject.Inject

val TAG = EventViewModel::class.java.simpleName

@HiltViewModel
class EventViewModel @Inject constructor(private val repository: EventRepository) : ViewModel() {
    val viewStates: MutableLiveData<List<EventViewState>> = MutableLiveData()

    init {
        viewModelScope.launch {
            repository.stateFlow.collect {
                if (it.isEmpty()) {
                    repository.insertBasicEvents()
                } else {
                    viewStates.postValue(it.map { event ->
                        EventViewState(event.id.toString(), event.name, event.description)
                    })
                }
            }
        }
//        repository.subject.subscribe {
//            viewStates.postValue(it.map { event ->
//                EventViewState(event.id.toString(), event.name, event.description)
//            })
//        }
//        viewModelScope.launch {
//            repository.getAllEvents()
//                .ifEmpty {
//                    repository.insertBasicEvents()
//                }
//        }
    }

    fun getData() {
        viewModelScope.launch {
            Log.d(TAG, repository.getAllEvents().toString())
            repository.getData()
        }
    }
}

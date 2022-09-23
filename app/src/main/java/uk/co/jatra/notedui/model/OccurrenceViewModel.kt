package uk.co.jatra.notedui.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uk.co.jatra.notedui.repositories.OccurrenceRepository
import uk.co.jatra.notedui.ui.EventRequestListener
import uk.co.jatra.notedui.ui.OccurrenceRequestListener
import uk.co.jatra.notedui.ui.OccurrenceViewState
import uk.co.jatra.notedui.util.SingleLiveEvent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OccurrenceViewModel @Inject constructor(private val repository: OccurrenceRepository) :
//class OccurrenceViewModel @ViewModelInject constructor(private val repository: OccurrenceRepository) :
    ViewModel(),
    EventRequestListener, OccurrenceRequestListener {
    private val disposables = CompositeDisposable()
    val viewStates: MutableLiveData<List<OccurrenceViewState>> = MutableLiveData()
    val date: MutableLiveData<String> = MutableLiveData()
    val showDatePicker: SingleLiveEvent<LocalDate> = SingleLiveEvent()

    private var internalDate: LocalDate = LocalDate.now()
        set(value) {
            field = value
            date.postValue(dateText(field))
            getDetails()
        }

    init {
        //FIXME this means we are listening for all updates from the repository,
        //even though the date of updates change.
        //eg when monitoring X days ago, a new Occurrence will cause an update.
//        disposables.add(
//            repository.occurrenceDetailsSubject.subscribe {
//                viewStates.postValue(it.map { details ->
//                    OccurrenceViewState(
//                        details.id,
//                        details.eventName,
//                        details.description,
//                        details.userId,
//                        timeString(details)
//                    )
//                })
////                date.postValue(dateText(internalDate))
//            })
        viewModelScope.launch {
            repository.occurrenceDetailsState
                .collect {
                    viewStates.postValue(it.map { details ->
                        OccurrenceViewState(
                            details.id,
                            details.eventName,
                            details.description,
                            details.userId,
                            timeString(details)
                        )
                    })
                }
        }
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        disposables.clear()
    }

    fun getDetails() {
        viewModelScope.launch {
            repository.getDetailsByDay(internalDate)
        }
    }

    override fun addTodayOccurrenceOfEvent(id: String) {
        today()
        viewModelScope.launch {
            repository.addOccurrence(id)
        }
    }

    override fun removeOccurrence(id: String) {
        viewModelScope.launch {
            repository.removeOccurrence(id)
        }
    }

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        internalDate = LocalDate.of(year, month + 1, dayOfMonth)
    }

    fun dateTextClicked() {
        showDatePicker.postValue(internalDate)
    }

    fun earlier() {
        internalDate = internalDate.minusDays(1)
    }

    fun later() {
        internalDate = internalDate.plusDays(1)
    }

    fun today() {
        internalDate = LocalDate.now()
    }

    fun archive() {
        viewModelScope.launch {
            repository.getAllDetails()
        }
    }

    private fun dateText(date: LocalDate) =
        date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))

    private fun timeString(details: OccurrenceDetail): String {
        val timeString = details.time
        return if (timeString.contains(':')) "at $timeString" else "at ${
            this.convertNanosOfDay(
                timeString
            )
        }"
    }

    private fun convertNanosOfDay(secondsString: String): String {
        return LocalTime.ofNanoOfDay(secondsString.toLong())
            .format(DateTimeFormatter.ofPattern("hh:mma"))
    }
}



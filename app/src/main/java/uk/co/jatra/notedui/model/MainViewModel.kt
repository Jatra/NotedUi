package uk.co.jatra.notedui.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import uk.co.jatra.notedui.repositories.OccurrenceRepository
import uk.co.jatra.notedui.ui.EventRequestListener
import uk.co.jatra.notedui.ui.OccurrenceRequestListener
import uk.co.jatra.notedui.ui.OccurrenceViewState

class MainViewModel(private val repository: OccurrenceRepository) : ViewModel(),
    EventRequestListener, OccurrenceRequestListener {
    val viewStates: MutableLiveData<List<OccurrenceViewState>> = MutableLiveData()
    val date: MutableLiveData<String> = MutableLiveData()
    val showDatePicker: MutableLiveData<LocalDate> = MutableLiveData()

    private var internalDate: LocalDate = LocalDate.now()
        set(value) {
            field = value
            date.postValue(dateText(value))
            getDetails()
        }

    init {
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

            date.postValue(dateText(internalDate))
        }

    }

    private fun dateText(date: LocalDate) =
        date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))

    fun getDetails() {
        repository.getDetailsByDay(internalDate)
    }

    override fun addTodayOccurrenceOfEvent(id: String) {
        repository.addOccurrence(id)
    }

    override fun removeOccurrence(id: String) {
        repository.removeOccurrence(id)
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
}

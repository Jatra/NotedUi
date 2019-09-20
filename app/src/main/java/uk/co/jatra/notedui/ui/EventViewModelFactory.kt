package uk.co.jatra.notedui.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.co.jatra.notedui.model.EventViewModel
import uk.co.jatra.notedui.repositories.EventRepository
import javax.inject.Inject

class EventViewModelFactory @Inject constructor(private val repository: EventRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventViewModel(repository) as T
    }

}

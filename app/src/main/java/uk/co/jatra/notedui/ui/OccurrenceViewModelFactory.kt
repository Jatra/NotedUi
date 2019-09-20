package uk.co.jatra.notedui.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.co.jatra.notedui.model.OccurrenceViewModel
import uk.co.jatra.notedui.repositories.OccurrenceRepository
import javax.inject.Inject

class OccurrenceViewModelFactory @Inject constructor(private val repository: OccurrenceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OccurrenceViewModel(repository) as T
    }

}

package uk.co.jatra.notedui.di

import dagger.Component
import uk.co.jatra.notedui.repositories.OccurrenceRepository
import uk.co.jatra.notedui.ui.MainActivity
import uk.co.jatra.notedui.ui.OccurrenceViewModelFactory
import javax.inject.Singleton

@Component(modules = [AppModule::class, PersistenceModule::class, SchedulerModule::class])
@Singleton
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: OccurrenceViewModelFactory)
    fun inject(target: OccurrenceRepository)
}

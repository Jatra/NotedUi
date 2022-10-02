package uk.co.jatra.notedui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

/**
 * Dagger module for providing Rx Schedulers.
 */
@Module
@InstallIn(SingletonComponent::class)
class SchedulerModule {

    /**
     * Provide the IO Scheduler on which to make data requests.
     *
     * @return the IO [Scheduler]
     */
    @Provides
    @Named("IOScheduler")
    fun providesIOScheduler(): Scheduler {
        return Schedulers.io()
    }

    /**
     * Provide the Main thread Scheduler on which to perform UI actions.
     *
     * @return the Android Main Thread [Scheduler]
     */
    @Provides
    @Named("Main")
    fun providesMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}


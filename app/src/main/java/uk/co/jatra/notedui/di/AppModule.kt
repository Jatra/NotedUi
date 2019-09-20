package uk.co.jatra.notedui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.jatra.notedui.NotedApplication
import javax.inject.Singleton

@Module
class AppModule(private val application: NotedApplication) {
    /**
     * provide the Application Context
     *
     * @return the Application [Context]
     */
    @Provides
    @Singleton
    fun providesContext(): Context = application

}

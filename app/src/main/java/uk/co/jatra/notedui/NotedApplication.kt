package uk.co.jatra.notedui

import android.app.Application
import uk.co.jatra.notedui.di.AppComponent
import uk.co.jatra.notedui.di.AppModule
import uk.co.jatra.notedui.di.DaggerAppComponent
import uk.co.jatra.notedui.di.PersistenceModule

class NotedApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: NotedApplication): AppComponent =
        //Component named DaggerX is defined by interface X, so see AppComponent
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .persistenceModule(PersistenceModule())
            .build()

}

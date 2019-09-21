package uk.co.jatra.notedui.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.events_sheet.*
import uk.co.jatra.notedui.NotedApplication
import uk.co.jatra.notedui.R
import uk.co.jatra.notedui.model.EventViewModel
import uk.co.jatra.notedui.model.OccurrenceViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var occurrenceViewModel: OccurrenceViewModel
    private lateinit var eventViewModel: EventViewModel
    @Inject
    lateinit var occurrenceViewModelFactory: OccurrenceViewModelFactory
    @Inject
    lateinit var eventViewModelFactory: EventViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInjector().inject(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        occurrenceViewModel =
            ViewModelProviders.of(this, occurrenceViewModelFactory)
                .get(OccurrenceViewModel::class.java)

        val occurrencesAdapter = OccurrencesAdapter(occurrenceViewModel)
        recycler.adapter = occurrencesAdapter
        recycler.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(occurrencesAdapter))
        itemTouchHelper.attachToRecyclerView(recycler)

        occurrenceViewModel.viewStates.observe(
            this,
            Observer<List<OccurrenceViewState>> { occurrences ->
                occurrencesAdapter.occurrences = occurrences
                ptr.isRefreshing = false
            }
        )

        occurrenceViewModel.getData()
        occurrenceViewModel.getDetails()

        ptr.setOnRefreshListener {
            occurrenceViewModel.getData()
            occurrenceViewModel.getDetails()
        }

        val sheetBehavior = BottomSheetBehavior.from(eventSheet)
        sheetBehavior.state = STATE_EXPANDED

        sheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(sheet: View, state: Int) {
                if (state == STATE_EXPANDED) {
                    fab.isVisible = false
                    val rows = eventList.childCount.coerceAtMost(3)
                    if (rows > 0) {
                        val height = rows * eventList[0].height
                        Log.d("sheet", "setting height to $height")
                        eventSheet.post {
                            eventSheet.layoutParams.height = height
                            eventSheet.requestLayout()
                            eventSheet.invalidate()
                        }
                    }
                }
                if (state == STATE_COLLAPSED || state == STATE_HIDDEN) {
                    fab.isVisible = true
                }
            }

        })

        val eventsAdapter = EventsAdapter(occurrenceViewModel)
        eventList.adapter = eventsAdapter
        eventList.layoutManager = LinearLayoutManager(this)

        eventViewModel =
            ViewModelProviders.of(this, eventViewModelFactory)
                .get(EventViewModel::class.java)
        eventViewModel.viewStates.observe(
            this,
            Observer<List<EventViewState>> { events ->
                eventsAdapter.events = events
            }
        )

        eventViewModel.getData()

        fab.setOnClickListener {
            sheetBehavior.state = STATE_EXPANDED
        }
    }

    private fun getInjector() = (application as NotedApplication).appComponent
}

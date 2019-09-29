package uk.co.jatra.notedui.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.events_sheet.*
import kotlinx.android.synthetic.main.events_sheet.view.*
import kotlinx.android.synthetic.main.occurrences_fragment.view.*
import org.threeten.bp.LocalDate
import uk.co.jatra.notedui.NotedApplication
import uk.co.jatra.notedui.R
import uk.co.jatra.notedui.model.EventViewModel
import uk.co.jatra.notedui.model.OccurrenceViewModel
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class OccurrenceFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        occurrenceViewModel.setDate(year, month, dayOfMonth)
    }

    private lateinit var occurrenceViewModel: OccurrenceViewModel
    private lateinit var eventViewModel: EventViewModel
    @Inject
    lateinit var occurrenceViewModelFactory: OccurrenceViewModelFactory
    @Inject
    lateinit var eventViewModelFactory: EventViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as NotedApplication).appComponent.inject(this)
        occurrenceViewModel = activity?.run {
            ViewModelProviders.of(this, occurrenceViewModelFactory)
                .get(OccurrenceViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        ViewModelProviders.of(this, occurrenceViewModelFactory)
            .get(OccurrenceViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.occurrences_fragment, container, false)

        val occurrencesAdapter = OccurrencesAdapter(occurrenceViewModel)
        root.recycler.adapter = occurrencesAdapter
        root.recycler.layoutManager = LinearLayoutManager(context)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(occurrencesAdapter))
        itemTouchHelper.attachToRecyclerView(root.recycler)

        occurrenceViewModel.viewStates.observe(
            this,
            Observer<List<OccurrenceViewState>> { occurrences ->
                occurrencesAdapter.occurrences = occurrences
                root.ptr.isRefreshing = false
            }
        )

        occurrenceViewModel.getDetails()

        occurrenceViewModel.date.observe(this, Observer<String> {
            root.dateText.text = it
        })

        occurrenceViewModel.showDatePicker.observe(this, Observer<LocalDate> {
            showDatePicker(it)
        })


        val sheetBehavior = BottomSheetBehavior.from(root.eventSheet).apply {
            state = STATE_EXPANDED
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(sheet: View, state: Int) {
                    if (state == STATE_EXPANDED) {
                        root.fab.isVisible = false
                        val rows = root.eventList.childCount.coerceAtMost(3)
                        if (rows > 0) {
                            val height = rows * root.eventList.getChildAt(0).height
                            Log.d("sheet", "setting height to $height")
                            eventSheet.post {
                                eventSheet.layoutParams.height = height
                                eventSheet.requestLayout()
                                eventSheet.invalidate()
                            }
                        }
                    }
                    if (state == STATE_COLLAPSED || state == STATE_HIDDEN) {
                        root.fab.isVisible = true
                    }
                }

            })
        }
        val eventsAdapter = EventsAdapter(occurrenceViewModel)
        root.eventList.adapter = eventsAdapter
        root.eventList.layoutManager = LinearLayoutManager(context)

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

        root.fab.setOnClickListener {
            sheetBehavior.state = STATE_EXPANDED
        }


        root.ptr.setOnRefreshListener {
            occurrenceViewModel.getDetails()
        }

        root.dateText.setOnClickListener {
            occurrenceViewModel.dateTextClicked()
        }

        root.earlier.setOnClickListener {
            occurrenceViewModel.earlier()
        }

        root.later.setOnClickListener {
            occurrenceViewModel.later()
        }

        root.today.setOnClickListener {
            occurrenceViewModel.today()
        }

        root.archive.setOnClickListener {
            occurrenceViewModel.archive()
        }

        return root
    }

    private fun showDatePicker(date: LocalDate) {
        DatePickerFragment.newInstance(date)
            .show(childFragmentManager, "datePicker")
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): OccurrenceFragment {
            return OccurrenceFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
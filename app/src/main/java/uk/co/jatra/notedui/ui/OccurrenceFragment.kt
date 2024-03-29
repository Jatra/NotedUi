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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.events_sheet.*
import kotlinx.android.synthetic.main.events_sheet.view.*
import kotlinx.android.synthetic.main.occurrences_fragment.view.*
import uk.co.jatra.notedui.R
import uk.co.jatra.notedui.model.EventViewModel
import uk.co.jatra.notedui.model.OccurrenceViewModel
import java.time.LocalDate

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class OccurrenceFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        occurrenceViewModel.setDate(year, month, dayOfMonth)
    }

    private val occurrenceViewModel: OccurrenceViewModel by viewModels()
    private val eventViewModel: EventViewModel by viewModels()

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
            viewLifecycleOwner,
            Observer<List<OccurrenceViewState>> { occurrences ->
                occurrencesAdapter.occurrences = occurrences
                root.ptr.isRefreshing = false
            }
        )

        occurrenceViewModel.getDetails()

        occurrenceViewModel.date.observe(viewLifecycleOwner, Observer<String> {
            root.dateText.text = it
        })

        occurrenceViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            showDatePicker(it)
        })


        val sheetBehavior = BottomSheetBehavior.from(root.eventSheet).apply {
            state = STATE_EXPANDED
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

        eventViewModel.viewStates.observe(
            viewLifecycleOwner,
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
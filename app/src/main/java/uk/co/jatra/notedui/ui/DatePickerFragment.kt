package uk.co.jatra.notedui.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import dagger.hilt.android.AndroidEntryPoint
import uk.co.jatra.notedui.model.OccurrenceViewModel
import java.time.LocalDate
import javax.inject.Inject

private const val DAY = "DAY"
private const val MONTH = "MONTH"
private const val YEAR = "YEAR"

@AndroidEntryPoint
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var occurrenceViewModelFactory: OccurrenceViewModelFactory
    private lateinit var model: OccurrenceViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        model = activity?.run {
            ViewModelProviders.of(this, occurrenceViewModelFactory)
                .get(OccurrenceViewModel::class.java)

        } ?: throw Exception("Invalid Activity")

        return DatePickerDialog(
            requireActivity(),
            this,
            arguments?.get(YEAR) as Int,
            arguments?.get(MONTH) as Int,
            arguments?.get(DAY) as Int
        )
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        model.setDate(year, month, day)
    }

    companion object {
        fun newInstance(date: LocalDate): DatePickerFragment {
            return DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putInt(DAY, date.dayOfMonth)
                    putInt(MONTH, date.monthValue - 1)
                    putInt(YEAR, date.year)
                }
            }
        }
    }

}
package uk.co.jatra.notedui.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.co.jatra.notedui.model.OccurrenceViewModel
import java.time.LocalDate

private const val DAY = "DAY"
private const val MONTH = "MONTH"
private const val YEAR = "YEAR"

@AndroidEntryPoint
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    //    @Inject
//    lateinit var occurrenceViewModelFactory: OccurrenceViewModelFactory
    private val model: OccurrenceViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

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
package uk.co.jatra.notedui.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.occurrence.view.*
import uk.co.jatra.notedui.R

class OccurrencesAdapter : RecyclerView.Adapter<OccurrenceViewHolder>() {

    var occurrences = emptyList<OccurrenceViewState>()
        set(value) {
            field = value.toList()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OccurrenceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.occurrence, parent, false)
        return OccurrenceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return occurrences.size
    }

    override fun onBindViewHolder(holder: OccurrenceViewHolder, position: Int) {
        val item = occurrences[position]
        holder.view.dataView.text = item.data
        holder.view.detailView.text = item.detail
        holder.view.whoView.text = item.who
        holder.view.timeView.text = item.time
    }

}

data class OccurrenceViewHolder(val view: View) : RecyclerView.ViewHolder(view)
package uk.co.jatra.notedui.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.occurrence.view.*

interface OccurrenceRequestListener {
    fun removeOccurrence(id: String)
}

class OccurrencesAdapter(private val listener: OccurrenceRequestListener) :
    RecyclerView.Adapter<OccurrenceViewHolder>() {

    private var recentlyDeletedItemPosition: Int? = null
    private var recentlyDeletedItem: OccurrenceViewState? = null
    var occurrences = emptyList<OccurrenceViewState>()
        set(value) {
            field = value.toList()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OccurrenceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(uk.co.jatra.notedui.R.layout.occurrence, parent, false)
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

    fun deleteItem(position: Int) {
        recentlyDeletedItem = occurrences[position]
        recentlyDeletedItemPosition = position
        listener.removeOccurrence(occurrences[position].id)
    }

}

data class OccurrenceViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class SwipeToDeleteCallback(private val adapter: OccurrencesAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }
}
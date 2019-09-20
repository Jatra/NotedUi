package uk.co.jatra.notedui.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.event.view.*
import uk.co.jatra.notedui.R

private const val TAG = "EventAdapter"

interface EventRequestListener {
    fun addOccurrenceOfEvent(id: String)
}

class EventsAdapter(private val listener: EventRequestListener) :
    RecyclerView.Adapter<EventViewHolder>() {

    private var visibleButton: Button? = null

    var events = emptyList<EventViewState>()
        set(value) {
            field = value.toList()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event, parent, false)
        view.setOnClickListener {
            setNowButtonVisible(view, !view.nowButton.isVisible)
            view.nowButton.setOnClickListener {
                setNowButtonVisible(view, false)
                val eventId = view.tag as String
                Log.d(TAG, "Add an occurrence of event $eventId dated NOW")
                this.listener.addOccurrenceOfEvent(eventId)
            }
        }
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = events[position]
        holder.view.nowButton.isVisible = false
        holder.view.nameView.text = item.name
        holder.view.descriptionView.text = item.description
        holder.view.nowButton.isVisible = false
        holder.view.tag = item.id
    }

    private fun setNowButtonVisible(view: View, visible: Boolean) {
        visibleButton?.let { previousButton ->
            if (previousButton != view.nowButton) {
                previousButton.isVisible = false
                visibleButton = null
            }
        }
        view.nowButton.isVisible = visible
        if (visible) visibleButton = view.nowButton
    }
}

data class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view)
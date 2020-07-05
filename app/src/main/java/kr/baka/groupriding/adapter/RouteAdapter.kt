package kr.baka.groupriding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.baka.groupriding.R
import kr.baka.groupriding.repository.room.entity.RouteEntity
import java.text.SimpleDateFormat
import java.util.*

class RouteAdapter(val contactItemClick: (RouteEntity) -> Unit,
                     val contactItemLongClick: (RouteEntity) -> Unit)
    : RecyclerView.Adapter<RouteAdapter.ViewHolder>() {

    private var routes: List<RouteEntity> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(routes[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.route_title)
        private val tvDistance = itemView.findViewById<TextView>(R.id.route_distance)
        private val tvDate = itemView.findViewById<TextView>(R.id.route_date)

        fun bind(route: RouteEntity) {

            tvTitle.text = route.name
            tvDistance.text = route.distance.toString()+"m"
            tvDate.text = SimpleDateFormat("yyyy-MM-dd").format(Date(route.date))

            itemView.setOnClickListener {
                contactItemClick(route)
            }

            itemView.setOnLongClickListener {
                contactItemLongClick(route)
                true
            }
        }
    }

    fun setContacts(routes: List<RouteEntity>) {
        this.routes = routes
        notifyDataSetChanged()
    }

}
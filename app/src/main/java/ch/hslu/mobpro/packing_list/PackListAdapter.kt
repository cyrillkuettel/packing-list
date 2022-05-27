package ch.hslu.mobpro.packing_list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.customviews.PacklistCardView
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel


class PackListAdapter(private val packlistViewModel: PacklistViewModel, val ctx: Context) :
    ListAdapter<Packlist, PackListAdapter.PacklistViewHolder>(Packlistcomparator()) {

    // Allows to remember the last item shown on screen
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacklistViewHolder {
        return PacklistViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PacklistViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current.title, current.location, current.date, current.color)

        holder.getView().setOnClickListener {
            // retrieve id of clicked item here
            Log.v(TAG, "setting clicked packlist title: ${current.title}")
            packlistViewModel.setClickedPacklist(current)
        }
        // animate the RecyclerView Items when there are appearing
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    fun getItemAt(pos: Int): Packlist? {
        return getItem(pos)
    }

    class PacklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: PacklistCardView = itemView.findViewById(R.id.packlistcardview)

        fun bind(text: String?, location: String?, date: String, color: Int) {

            cardView.setTitle(text)
            cardView.setLocation(location)
            cardView.setDate(date)
            cardView.setColor(color)
        }


        fun getView(): PacklistCardView {
            return cardView
        }


        companion object {
            fun create(parent: ViewGroup): PacklistViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.packlist_item, parent, false)

                return PacklistViewHolder(view)
            }

        }

    }

    class Packlistcomparator : DiffUtil.ItemCallback<Packlist>() {
        override fun areItemsTheSame(oldItem: Packlist, newItem: Packlist): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Packlist, newItem: Packlist): Boolean {
            return oldItem.title == newItem.title
        }
    }

    companion object {
        private const val TAG = "PackListAdapter"
    }

}
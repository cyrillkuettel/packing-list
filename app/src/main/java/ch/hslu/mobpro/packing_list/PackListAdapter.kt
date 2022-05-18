package ch.hslu.mobpro.packing_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import ch.hslu.mobpro.packing_list.customviews.PacklistCardView


class PackListAdapter(private val packlistViewModel: PacklistViewModel) :
    ListAdapter<Packlist, PackListAdapter.PacklistViewHolder>(Packlistcomparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacklistViewHolder {
        return PacklistViewHolder.create(parent)
    }



    override fun onBindViewHolder(holder: PacklistViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.title, current.location)

        holder.getView().setOnClickListener {
            // retrieve id of clicked item here
            Log.v(TAG, "setting clicked packlist title: ${current.title}")
            packlistViewModel.setClickedPacklist(current)
        }
    }

    class PacklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: PacklistCardView = itemView.findViewById(R.id.packlistcardview)

        fun bind(text: String?,location: String?) {
            cardView.setTitle(text)
            cardView.setLocation(location)
            // cardView.height = cardView.getCurrentViewHeight()
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
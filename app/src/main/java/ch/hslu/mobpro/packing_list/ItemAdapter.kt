package ch.hslu.mobpro.packing_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel


class ItemAdapter(private val itemViewModel: ItemViewModel) :

    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.content)

        holder.getView().setOnClickListener {
            Log.v(TAG, "clicked on item wit content: ${current.content}")
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemContent: TextView = itemView.findViewById(R.id.itemContent)

        fun bind(content: String?) {
            itemContent.text = content
        }

        fun getView(): TextView {
            return itemContent
        }

        companion object {
            fun create(parent: ViewGroup): ItemViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item, parent, false)
                return ItemViewHolder(view)
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.content == newItem.content
        }
    }

    companion object {
        private const val TAG = "ItemAdapter"
    }

}
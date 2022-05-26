package ch.hslu.mobpro.packing_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.fragments.PacklistFragment
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel


class ItemAdapter(private val itemViewModel: ItemViewModel, val lifeCycleOwner: LifecycleOwner) :

    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        Log.v(TAG, current.itemContentID.toString())

        holder.bind(current.content)
        //holder.setStatus(current.status)
        holder.getView().setOnClickListener {
            Log.v(TAG, "clicked on text of item with content: ${current.content}")
        }



        // first, find the checkbox status as stored in the DB.
        itemViewModel.getStatus(current.itemContentID).observe(lifeCycleOwner) { items ->
            val item: Item = items[0]
            // Then update the value in the UI
            holder.setStatus(item.status)
            Log.v(TAG, "current status of this item is ${item.status}")

        }

        holder.getStatus().setOnClickListener {
            itemViewModel.setStatus(current.itemContentID, !current.status)

            //Log.v(TAG, "clicked on Cb from Item with checkbox status: ${getItem(position).status}")

        }
    }




    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        // Text View in Item
        private val itemContent: TextView = itemView.findViewById(R.id.itemContent)

        // Check Box in Item
        private val itemStatus: CheckBox = itemView.findViewById(R.id.itemCb)

        // String from TextView
        fun bind(content: String?) {
            itemContent.text = content
        }
        fun getView(): TextView {
            return itemContent
        }

        // Status from CheckBox
        fun setStatus(status: Boolean){
            itemStatus.isChecked = status
        }
        fun getStatus(): CheckBox{
            return itemStatus
        }

        // static method to create a new item
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
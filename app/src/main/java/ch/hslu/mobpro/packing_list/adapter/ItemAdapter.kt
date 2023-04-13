package ch.hslu.mobpro.packing_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.packing_list.R
import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.viewmodels.ItemViewModel


/**
 * A custom Adapter for the itemRecyclerView. It is used to display the items of a single Packlist.
 * To be able to interact with the rest of the App, the itemViewModel is injected.
 * If one item is clicked, the onClickListener fires. This logic is implemented here.
 */
class ItemAdapter(private val itemViewModel: ItemViewModel, val lifeCycleOwner: LifecycleOwner) :
    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.content, current.color)
        holder.getView().setOnClickListener {
            Log.v(TAG, "clicked on View of item with content: ${current.content}")
        }


        observeViewModels(current, holder)

        holder.getStatus().setOnClickListener {
            itemViewModel.setStatus(current.itemContentID, !current.status)
            Log.v(TAG, "clicked on Cb from Item with checkbox status: ${getItem(position).status}")
        }

        holder.getItem().setOnClickListener {
            Log.d(TAG, "clicked on the item itself")
        }


    }

    private fun observeViewModels(
        current: Item,
        holder: ItemViewHolder
    ) {
        // Retrieve Checkbox status from database.
        // We can only access the DB by observing the viewModel. To make this work, the
        // viewLifecycleOwner form the fragment is passed in. (Manual dependency injection)
        itemViewModel.getStatus(current.itemContentID).observe(lifeCycleOwner) { items ->
            if (items.isNotEmpty()) {
                // first, find the checkbox status as stored in the DB.
                val item: Item = items[0]
                // Then update the value in the UI
                holder.setStatus(item.status)
                Log.v(TAG, "current status of this item is ${item.status}")
            }
        }
    }

    fun getItemAt(pos: Int): Item? {
        return getItem(pos)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val item: CardView = itemView.findViewById(R.id.item)

        // Text View in Item
        private val itemContent: TextView = itemView.findViewById(R.id.itemContent)

        // Check Box in Item
        private val itemStatus: CheckBox = itemView.findViewById(R.id.itemCb)

        // String from TextView
        fun bind(content: String?, color: Int) {
            itemContent.text = content
            item.setBackgroundColor(color)
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

        fun getItem(): CardView {
            return item
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
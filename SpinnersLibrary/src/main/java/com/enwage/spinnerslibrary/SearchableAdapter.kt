package com.enwage.spinnerslibrary

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class SearchableAdapter<T>(
    private val context: Context,
    private val originalList: List<SearchableItemMulti >,
    private val itemClickListener: ItemClickListener<T>
) : RecyclerView.Adapter<SearchableAdapter<T>.ViewHolder>(), Filterable {

    private var filteredList: MutableList<SearchableItemMulti> = originalList.toMutableList()

    interface ItemClickListener<T> {
        fun onItemClicked(item: SearchableItemMulti, isChecked: Boolean)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        internal var titleTextView = mView.findViewById<TextView>(R.id.titleTextView)
        internal var checkBox = mView.findViewById<CheckBox>(R.id.checkBox)

        var mItem: SearchableItemMulti? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_multi_select_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val item = filteredList[position]

            // Log the item's state for debugging
            Log.d("SelectedItem", "Item: ${item.name}, isSelected: ${item.isSelected}")

            // Bind the data to the views
            holder.titleTextView.text = item.name
            holder.checkBox.setOnCheckedChangeListener(null) // Clear any previous listeners
            holder.checkBox.isChecked = item.isSelected

            // Handle checkbox state changes
            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isSelected = isChecked // Update the item's state
                itemClickListener.onItemClicked(item, isChecked) // Notify the listener
            }

            // Optional: Row click to toggle selection
            holder.mView.setOnClickListener {
                val newCheckedState = !holder.checkBox.isChecked
                holder.checkBox.isChecked = newCheckedState
                item.isSelected = newCheckedState
                itemClickListener.onItemClicked(item, newCheckedState)
            }
        } catch (e: Exception) {
            Log.d("exp", e.toString())
        }
    }







    override fun getItemCount() = filteredList.size



    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val filteredItems = mutableListOf<SearchableItemMulti>()

                if (constraint == null || constraint.isEmpty()) {
                    filteredItems.addAll(originalList) // No filtering
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()

                    for (item in originalList) {
                        if (item.name.toLowerCase().contains(filterPattern)) {
                            filteredItems.add(item)
                        }
                    }
                }

                filterResults.values = filteredItems
                filterResults.count = filteredItems.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<SearchableItemMulti>
                notifyDataSetChanged()
            }
        }
    }
}

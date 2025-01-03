package com.enwage.spinnerslibrary.spinner

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.enwage.spinnerslibrary.R
import com.enwage.spinnerslibrary.SearchableItemMulti



internal class SpinnerRecyclerAdapter(
    private val context: Context,
    private val originalList: ArrayList<SearchableItemMulti>, // Original unfiltered list
    private val onItemSelectListener: OnItemSelectListener
) : RecyclerView.Adapter<SpinnerRecyclerAdapter.SpinnerHolder>() {

    private val filteredList = ArrayList(originalList) // Mutable list for filtered items
    private val selectedItemIds = mutableSetOf<Int>() // Track selected items by their unique IDs

    var onSelectAllStateChanged: ((Boolean) -> Unit)? = null
    var highlightSelectedItem: Boolean = true
    private val selectedItems = mutableSetOf<Int>() // Track selected items by their position
    // Method to initialize adapter data
    fun toggleSelectAll(isChecked: Boolean) {
        selectedItemIds.clear() // Clear current selection
        if (isChecked) {
            originalList.forEach { item ->
                selectedItemIds.add(item.id)
                item.isSelected = true
            }
        } else {
            originalList.forEach { item ->
                item.isSelected = false
            }
        }
        notifyDataSetChanged()
    }


    fun initializeData() {
        selectedItemIds.clear() // Clear previous selections
        originalList.forEach { item ->
            if (item.isSelected) {
                selectedItemIds.add(item.id) // Populate selected IDs
            }
        }
        filteredList.clear()
        filteredList.addAll(originalList.distinctBy { it.id }) // Avoid duplication
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerHolder {
        return SpinnerHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_seachable_spinner,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: SpinnerHolder, position: Int) {
        val currentItem = filteredList[position]

        // Bind data to the view holder
        holder.textViewSpinnerItem.text = currentItem.name
        holder.checkbox.isChecked = selectedItemIds.contains(currentItem.id)

        // Handle checkbox click
        holder.checkbox.setOnClickListener {
            if (selectedItemIds.contains(currentItem.id)) {
                selectedItemIds.remove(currentItem.id)
                currentItem.isSelected = false
            } else {
                selectedItemIds.add(currentItem.id)
                currentItem.isSelected = true
            }

            // Notify "Select All" state based on current selection
            val allSelected = originalList.all { it.isSelected }
            onSelectAllStateChanged?.invoke(allSelected)

            notifyItemChanged(position)
        }
    }


    class SpinnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewSpinnerItem: TextView = itemView.findViewById(R.id.textViewSpinnerItem)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    // Filter the list based on the search query
    fun filter(query: CharSequence?) {
        filteredList.clear() // Clear filtered list to avoid duplication
        if (query.isNullOrEmpty()) {
            filteredList.addAll(originalList.distinctBy { it.id }) // Reset to unique original list
        } else {
            filteredList.addAll(originalList.filter { it.name.contains(query, ignoreCase = true) }.distinctBy { it.id })
        }
        notifyDataSetChanged()
    }

    // Get the selected items
    fun getSelectedItems(): List<SearchableItemMulti> {
        return originalList.filter { it.isSelected }
    }
}













//
//
//
//
//internal class SpinnerRecyclerAdapter(
//    private val context: Context,
//    private val list: ArrayList<SearchableItemMulti>,
//    private val onItemSelectListener: ItemClickListener
//) : RecyclerView.Adapter<SpinnerRecyclerAdapter.SpinnerHolder>(), Filterable {
//
//
//    private var filteredList: MutableList<SearchableItemMulti> = list.toMutableList()
//
//    var highlightSelectedItem: Boolean = true
//    private val selectedItems = mutableSetOf<Int>()  // Track selected items by their position
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerHolder {
//        return SpinnerHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.list_item_seachable_spinner,
//                parent,
//                false
//            )
//        )
//    }
//
//    interface ItemClickListener {
//        fun onItemClicked(item: SearchableItemMulti, isChecked: Boolean)
//    }
//
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//
//
//
//    override fun onBindViewHolder(holder: SpinnerHolder, position: Int) {
//
//        try {
//
//
//            val item = filteredList[position]
//
//
//            Log.d("SelectedItem", "Item: ${item.name}, isSelected: ${item.isSelected}")
//
//            holder.titleTextView.text = item.name
//            holder.checkBox.setOnCheckedChangeListener(null) // Clear any previous listeners
//            holder.checkBox.isChecked = item.isSelected
//
//            // Handle checkbox state changes
//            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//                item.isSelected = isChecked // Update the item's state
//                onItemSelectListener.onItemClicked(item, isChecked) // Notify the listener
//            }
//
//            // Optional: Row click to toggle selection
//            holder.mView.setOnClickListener {
//                val newCheckedState = !holder.checkBox.isChecked
//                holder.checkBox.isChecked = newCheckedState
//                item.isSelected = newCheckedState
//                onItemSelectListener.onItemClicked(item, newCheckedState)
//            }
//        }catch (E:Exception)
//        {
//
//        }
//
//
//    }
//
//
//
//    inner class SpinnerHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        internal var titleTextView = mView.findViewById<TextView>(R.id.titleTextView)
//        internal var checkBox = mView.findViewById<CheckBox>(R.id.checkBox)
//
//        var mItem: SearchableItemMulti? = null
//    }
//
//    // Method to filter the list based on a search query
//    fun filter(query: CharSequence?) {
//        val filteredItems = ArrayList<SearchableItemMulti>()
//        if (query.isNullOrEmpty()) {
//            filterList(list)
//        } else {
//            for (item in list) {
//                if (item.name.contains(query, true)) {
//                    filteredItems.add(item)
//                }
//            }
//            filterList(filteredItems)
//        }
//    }
//
//    // Update the list based on the filtered results
//    private fun filterList(filteredItems: ArrayList<SearchableItemMulti>) {
//        list.clear()
//        list.addAll(filteredItems)
//        notifyDataSetChanged()
//    }
//
//    // Get the selected items data (when Done is clicked)
//    fun getSelectedItems(): List<SearchableItemMulti> {
//        val selectedData = mutableListOf<SearchableItemMulti>()
//        for (index in selectedItems) {
//            selectedData.add(list[index])  // Retrieve the selected items from the list
//        }
//        return selectedData
//    }
//
//
//    override fun getFilter(): android.widget.Filter {
//        return object : android.widget.Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val filterResults = FilterResults()
//                val filteredItems = mutableListOf<SearchableItemMulti>()
//
//                if (constraint == null || constraint.isEmpty()) {
//                    filteredItems.addAll(list) // No filtering
//                } else {
//                    val filterPattern = constraint.toString().toLowerCase().trim()
//
//                    for (item in list) {
//                        if (item.name.toLowerCase().contains(filterPattern)) {
//                            filteredItems.add(item)
//                        }
//                    }
//                }
//
//                filterResults.values = filteredItems
//                filterResults.count = filteredItems.size
//                return filterResults
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                filteredList = results?.values as MutableList<SearchableItemMulti>
//                notifyDataSetChanged()
//            }
//        }
//    }
//
//
//}


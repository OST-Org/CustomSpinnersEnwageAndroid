package com.enwage.spinnerslibrary


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




class SearchableMultiSelectSpinner<T> {
    companion object {
        @SuppressLint("MissingInflatedId")
        fun <T> show(
            context: Context,
            title: String,
            doneButtonText: String,
            cancelButtonText: String,
            SearchableItemMultis: MutableList<SearchableItemMulti>,
            selectionCompleteListener: SelectionCompleteListener<T>,
            selectedItems: List<SearchableItemMulti> = emptyList() // Add this parameter for pre-selected items
        ) {


            val dialog = Dialog(context, R.style.FullScreenWhiteDialog) // Full-screen style
            dialog.setContentView(R.layout.dialog_spinner_with_search)
            dialog.setTitle(title)


            val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
            val searchView =
                dialog.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)

            val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
            val tvDone = dialog.findViewById<TextView>(R.id.tvDone)
            val ivCancel = dialog.findViewById<ImageView>(R.id.ivCancel)

            // val filteredList = list.toMutableList()
            //   val adapter = MultiSelectAdapter(filteredList, selectedItems)
            recyclerView.layoutManager = LinearLayoutManager(context)
            // recyclerView.adapter = adapter


//
//            val alertDialog = AlertDialog.Builder(context)
//            val inflater = LayoutInflater.from(context)
            //           val convertView = inflater.inflate(R.layout.searchable_list_layout, null)
//
//            alertDialog.setView(convertView)
//           // alertDialog.setTitle(title)

            //     val searchView = convertView.findViewById<SearchView>(R.id.searchView)
            // val ivCross = convertView.findViewById<ImageView>(R.id.iv_crosss)
            // val recyclerView = convertView.findViewById<RecyclerView>(R.id.recyclerView)
            val layoutManager = LinearLayoutManager(context)
            // Mark the pre-selected items as selected
            SearchableItemMultis.forEach { item ->
                if (selectedItems.contains(item)) {
                    item.isSelected = true
                }
            }

            val adapter = SearchableAdapter(
                context,
                SearchableItemMultis,
                object : SearchableAdapter.ItemClickListener<T> {
                    override fun onItemClicked(item: SearchableItemMulti, isChecked: Boolean) {
                        item.isSelected = isChecked
                    }
                }
            )

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter



            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }
            })

            ivCancel.setOnClickListener {
                dialog.dismiss()
            }

//            alertDialog.setNegativeButton(cancelButtonText) { dialog, _ ->
//                dialog.dismiss()
//            }
//


            tvDone.setOnClickListener {

                    dialog.dismiss()
                    val selectedItems = SearchableItemMultis.filter { it.isSelected }
                    selectionCompleteListener.onCompleteSelection(ArrayList(selectedItems))

                }





                dialog.create()

                dialog.show()
            }

        }

}


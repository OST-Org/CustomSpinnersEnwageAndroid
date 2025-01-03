package com.Enwage.ostspinners

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.Enwage.ostspinners.databinding.ActivityMainBinding
import com.enwage.spinnerslibrary.MainActivity.Companion.showSpinnerDialog
import com.enwage.spinnerslibrary.SearchableItemMulti
import com.enwage.spinnerslibrary.SearchableMultiSelectSpinner
import com.enwage.spinnerslibrary.SelectionCompleteListener
import com.enwage.spinnerslibrary.spinner.OnItemSelectListener

import com.enwage.spinnerslibrary.spinner.SearchableSpinner

class MainActivity : AppCompatActivity(){



    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }





//
//        val spinner = SearchableSpinner(this)
//        spinner.setSpinnerListItems(items2) // Pass the list with preselected items
//
//        binding.autoCompleteTextView.setOnClickListener {
//            spinner.show("Select Client")
//        }
//
//        spinner.onItemSelectListener = object : OnItemSelectListener {
//            override fun setOnItemSelectListener(id: Int, selectedString: String) {
//                Log.d("activitystring", "Selected ID: $id, Name: $selectedString")
//            }
//        }
//


//    private var selectedItemsList = mutableListOf<SearchableItemMulti>()
//
//    val items2 = arrayListOf(
//        SearchableItemMulti(1, "Atlantis",true),
//        SearchableItemMulti(2, "Ocean"),
//        SearchableItemMulti(3, "Alaska"),
//        SearchableItemMulti(4, "Okara" ),
//        SearchableItemMulti(5, "Vehari"),
//        SearchableItemMulti(6, "Islamabad"),
//        SearchableItemMulti(7, "Chinote"),
//        SearchableItemMulti(8, "London"),
//        SearchableItemMulti(9, "Birmingham"),
//        SearchableItemMulti(10, "Liverpool"),
//        SearchableItemMulti(11, "Moscow"),
//        SearchableItemMulti(12, "Texas"),
//        SearchableItemMulti(13, "Dallas"),
//        SearchableItemMulti(14, "Washigton"),
//        SearchableItemMulti(15, "Kamoki"),
//        SearchableItemMulti(16, "Italy"),
//        SearchableItemMulti(17, "Penselvenya",),
//        SearchableItemMulti(18, "Washigton"),
//        SearchableItemMulti(19, "Kamoki"),
//        SearchableItemMulti(20, "Italy"),
//        SearchableItemMulti(21, "Penselvenya"),
//    )


//    fun showfun() {
//        val items = listOf(
//            SearchableItemMulti(1, "Option 1"),
//            SearchableItemMulti(2, "Option 2"),
//            SearchableItemMulti(3, "Option 3"),
//            SearchableItemMulti(4, "Option 4"),
//            SearchableItemMulti(5, "Option 5"),
//            SearchableItemMulti(6, "Option 6"),
//            SearchableItemMulti(7, "Option 7"),
//            SearchableItemMulti(8, "Option 8"),
//            SearchableItemMulti(9, "Option 9"),
//            SearchableItemMulti(10, "Option 10"),
//            SearchableItemMulti(11, "Option 11"),
//            SearchableItemMulti(12, "Option 12"),
//            SearchableItemMulti(13, "Option 13"),
//            SearchableItemMulti(14, "Option 14"),
//            SearchableItemMulti(15, "Option 15"),
//            SearchableItemMulti(16, "Option 16"),
//            SearchableItemMulti(17, "Option 17"),
//            SearchableItemMulti(18, "Option 18")
//        )
//
//        // Use selectedItemsList if it’s not empty, else use items
//        val listToShow = if (selectedItemsList.isNotEmpty()) {
//            // Map the items to include the selected state from selectedItemsList
//            items.map { item ->
//                val selectedItem = selectedItemsList.find { it.id == item.id }
//                if (selectedItem != null) {
//                    // If item is in selectedItemsList, mark it as selected
//                    item.copy(isSelected = true)
//                } else {
//                    // Otherwise, leave it unselected
//                    item.copy(isSelected = false)
//                }
//            }.toMutableList()
//        } else {
//            // If selectedItemsList is empty, just use the default items without selection
//            items.map { it.copy(isSelected = false) }.toMutableList()
//        }
//
//        // Pass the selected items to the dialog
//        SearchableMultiSelectSpinner.show(
//            this,
//            "Select Options",
//            "Done",
//            "Cancel",
//            listToShow,
//            object : SelectionCompleteListener<Int> {
//                override fun onCompleteSelection(selectedItems: ArrayList<SearchableItemMulti>) {
//                    Log.d("Selected Items", selectedItems.toString())
//                    binding.editTextText.setText(selectedItems.toString())
//                    selectedItemsList.clear()  // Clear the previous list
//                    selectedItemsList.addAll(selectedItems)  // Add the selected items to the list
//                }
//            },
//            selectedItemsList // Pass the selected items when reopening the dialog
//        )
//    }

//    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItemMulti>) {
//        Log.d("Selected Items", selectedItems.toString())
//    }
//

}
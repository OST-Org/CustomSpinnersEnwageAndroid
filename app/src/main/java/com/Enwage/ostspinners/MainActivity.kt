package com.Enwage.ostspinners

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.Enwage.ostspinners.databinding.ActivityMainBinding
import com.enwage.spinnerslibrary.MainActivity.Companion.showSpinnerDialog
import com.enwage.spinnerslibrary.SearchableItem
import com.enwage.spinnerslibrary.SearchableItem2
import com.enwage.spinnerslibrary.SearchableMultiSelectSpinner
import com.enwage.spinnerslibrary.SelectionCompleteListener
import com.enwage.spinnerslibrary.spinner.OnItemSelectListener

import com.enwage.spinnerslibrary.spinner.SearchableSpinner

class MainActivity : AppCompatActivity(){



    lateinit var binding: ActivityMainBinding

    private var selectedItemsList = mutableListOf<SearchableItem>()

    val items2 = arrayListOf(
        SearchableItem(1, "Atlantis",true),
        SearchableItem(2, "Ocean"),
        SearchableItem(3, "Alaska"),
        SearchableItem(4, "Okara" ),
        SearchableItem(5, "Vehari"),
        SearchableItem(6, "Islamabad"),
        SearchableItem(7, "Chinote"),
        SearchableItem(8, "London"),
        SearchableItem(9, "Birmingham"),
        SearchableItem(10, "Liverpool"),
        SearchableItem(11, "Moscow"),
        SearchableItem(12, "Texas"),
        SearchableItem(13, "Dallas"),
        SearchableItem(14, "Washigton"),
        SearchableItem(15, "Kamoki"),
        SearchableItem(16, "Italy"),
        SearchableItem(17, "Penselvenya",),
        SearchableItem(18, "Washigton"),
        SearchableItem(19, "Kamoki"),
        SearchableItem(20, "Italy"),
        SearchableItem(21, "Penselvenya"),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)








        val spinner = SearchableSpinner(this)
        spinner.setSpinnerListItems(items2) // Pass the list with preselected items

        binding.autoCompleteTextView.setOnClickListener {
            spinner.show("Select Client")
        }

        spinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(id: Int, selectedString: String) {
                Log.d("activitystring", "Selected ID: $id, Name: $selectedString")
            }
        }





    }







    fun showfun() {
        val items = listOf(
            SearchableItem(1, "Option 1"),
            SearchableItem(2, "Option 2"),
            SearchableItem(3, "Option 3"),
            SearchableItem(4, "Option 4"),
            SearchableItem(5, "Option 5"),
            SearchableItem(6, "Option 6"),
            SearchableItem(7, "Option 7"),
            SearchableItem(8, "Option 8"),
            SearchableItem(9, "Option 9"),
            SearchableItem(10, "Option 10"),
            SearchableItem(11, "Option 11"),
            SearchableItem(12, "Option 12"),
            SearchableItem(13, "Option 13"),
            SearchableItem(14, "Option 14"),
            SearchableItem(15, "Option 15"),
            SearchableItem(16, "Option 16"),
            SearchableItem(17, "Option 17"),
            SearchableItem(18, "Option 18")
        )

        // Use selectedItemsList if it’s not empty, else use items
        val listToShow = if (selectedItemsList.isNotEmpty()) {
            // Map the items to include the selected state from selectedItemsList
            items.map { item ->
                val selectedItem = selectedItemsList.find { it.id == item.id }
                if (selectedItem != null) {
                    // If item is in selectedItemsList, mark it as selected
                    item.copy(isSelected = true)
                } else {
                    // Otherwise, leave it unselected
                    item.copy(isSelected = false)
                }
            }.toMutableList()
        } else {
            // If selectedItemsList is empty, just use the default items without selection
            items.map { it.copy(isSelected = false) }.toMutableList()
        }

        // Pass the selected items to the dialog
        SearchableMultiSelectSpinner.show(
            this,
            "Select Options",
            "Done",
            "Cancel",
            listToShow,
            object : SelectionCompleteListener<Int> {
                override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                    Log.d("Selected Items", selectedItems.toString())
                    binding.editTextText.setText(selectedItems.toString())
                    selectedItemsList.clear()  // Clear the previous list
                    selectedItemsList.addAll(selectedItems)  // Add the selected items to the list
                }
            },
            selectedItemsList // Pass the selected items when reopening the dialog
        )
    }

//    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
//        Log.d("Selected Items", selectedItems.toString())
//    }
//

}
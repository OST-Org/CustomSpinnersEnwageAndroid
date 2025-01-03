package com.enwage.spinnerslibrary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Helooo from library", Toast.LENGTH_SHORT).show()

    }



    companion object{
        fun  showSpinnerDialog(
            context: Context,
            title: String,
            doneButtonText: String,
            cancelButtonText: String,
            items: List<SearchableItem>,
        ) {


        var selectedItemsList = mutableListOf<SearchableItem>()

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
                context,
                "Select Options",
                "Done",
                "Cancel",
                listToShow,
                object : SelectionCompleteListener<Int> {
                    override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                        Log.d("Selected Items", selectedItems.toString())
                      //  binding.editTextText.setText(selectedItems.toString())
                        selectedItemsList.clear()  // Clear the previous list
                        selectedItemsList.addAll(selectedItems)  // Add the selected items to the list
                    }
                },
                selectedItemsList // Pass the selected items when reope````````````````````````````ning the dialog
            )
        }


    }























}
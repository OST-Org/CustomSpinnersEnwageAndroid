package com.enwage.spinnerslibrary.spinner


import android.widget.EditText


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.enwage.spinnerslibrary.R
import com.enwage.spinnerslibrary.SearchableItem

@Suppress("MemberVisibilityCanBePrivate", "RedundantSetter", "RedundantGetter")
class SearchableSpinner(private val context: Context) : LifecycleObserver {
    private val handler = Handler(Looper.getMainLooper())


    lateinit var onItemSelectListener: OnItemSelectListener
    private lateinit var dialog: AlertDialog
    var datalist: ArrayList<SearchableItem> = ArrayList()
    private lateinit var dialogView: View
    var isfromsearch = false
    private lateinit var recyclerAdapter: SpinnerRecyclerAdapter

    var windowTopBackgroundColor: Int? = null
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }

    var windowTitleTextColor: Int = ContextCompat.getColor(context, android.R.color.white)
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }

    var negativeButtonBackgroundColor: Int? = null
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }

    var negativeButtonTextColor: Int = ContextCompat.getColor(context, android.R.color.white)
        @ColorInt get() = field
        set(@ColorInt colorInt) {
            field = colorInt
        }

    var isSearchAppliedFromHome: Boolean = false
    var animationDuration: Int = 420
    var showKeyboardByDefault: Boolean = true
    var spinnerCancelable: Boolean = false
    var windowTitle: String? = null
    var searchQueryHint: String = context.getString(android.R.string.search_go)
    var negativeButtonText: String = context.getString(android.R.string.cancel)
    var dismissSpinnerOnItemClick: Boolean = true
    var highlightSelectedItem: Boolean = true
    var negativeButtonVisibility: SpinnerView = SpinnerView.VISIBLE
    var windowTitleVisibility: SpinnerView = SpinnerView.GONE
    var searchViewVisibility: SpinnerView = SpinnerView.VISIBLE
    var selectedItemPosition: Int = -1
    var selectedItem: String? = null
    lateinit var textViewTitle: TextView
    lateinit var searchview: SearchView
    lateinit var buttonClose: Button
    lateinit var recyclerView: RecyclerView
    lateinit var headLayouts: RelativeLayout
    lateinit var headLayout: LinearLayout
    lateinit var btnSubmit: Button
    lateinit var btncancel: Button
    val Colorlabel = "#0c2d48"

    @Suppress("unused")
    enum class SpinnerView(val visibility: Int) {
        VISIBLE(View.VISIBLE),
        INVISIBLE(View.INVISIBLE),
        GONE(View.GONE)
    }

    init {
        initLifeCycleObserver()
    }

    fun show(text: String) {
        if (getDialogInfo(true)) {
            dialogView = View.inflate(context, R.layout.searchable_spinner, null)

            textViewTitle = dialogView.findViewById(R.id.textViewTitle)
            searchview = dialogView.findViewById(R.id.searchView2)
            buttonClose = dialogView.findViewById(R.id.buttonClose)
            recyclerView = dialogView.findViewById(R.id.recyclerView)
            headLayout = dialogView.findViewById(R.id.headLayout)
            btnSubmit = dialogView.findViewById(R.id.button)
            btncancel = dialogView.findViewById(R.id.cancel)
            val selectAllCheckBox = dialogView.findViewById<CheckBox>(R.id.selectAllCheckBox)

            val originalState = datalist.map { it.copy() }

            // Check if all items are selected and update "Select All" checkbox
            selectAllCheckBox.isChecked = recyclerAdapter.getSelectedItems().size == datalist.size

            // Set "Select All" functionality
            handleSelectAll(selectAllCheckBox)





            // Listen for changes in individual checkbox selections

            val dialogBuilder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)

            // "Select All" checkbox behavior
            selectAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
                recyclerAdapter.toggleSelectAll(isChecked)
            }

            // Update "Select All" checkbox when individual checkboxes are clicked
            recyclerAdapter.onSelectAllStateChanged = { allSelected ->
                selectAllCheckBox.setOnCheckedChangeListener(null) // Temporarily remove listener
                selectAllCheckBox.isChecked = allSelected
                selectAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    recyclerAdapter.toggleSelectAll(isChecked)
                }
            }

            recyclerView.adapter = recyclerAdapter

            // Customize UI
            val searchEditText = searchview.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            searchEditText?.apply {
                setTextColor(Color.WHITE)
                setHintTextColor(Color.LTGRAY)
            }
            val background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(Color.parseColor(Colorlabel))
                cornerRadius = 20f
            }
            headLayout.background = background
            btnSubmit.backgroundTintList = ColorStateList.valueOf(Color.parseColor(Colorlabel))
            btnSubmit.setTextColor(Color.WHITE)
            btncancel.backgroundTintList = ColorStateList.valueOf(Color.parseColor(Colorlabel))
            btncancel.setTextColor(Color.WHITE)


            // "Cancel" button functionality
            btncancel.setOnClickListener {
                // Restore the original state
                datalist.clear()
                datalist.addAll(originalState)

                recyclerAdapter.initializeData()
                dialog.dismiss()
            }
            // Show the dialog
            dialog = dialogBuilder.create()
            dialog.initView()
            dialog.show()
        }
    }

    private fun handleSelectAll(selectAllCheckBox: CheckBox) {
        selectAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
            recyclerAdapter.toggleSelectAll(isChecked)
        }
    }

    fun dismiss() {
        if (getDialogInfo(false))
            CircularReveal.startReveal(false, dialog, object : OnAnimationEnd {
                override fun onAnimationEndListener(isRevealed: Boolean) {
                    toggleKeyBoard(false)
                    if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(null)
                }
            }, animationDuration)
    }

    fun setSpinnerListItems(spinnerList: ArrayList<SearchableItem>) {
        datalist.clear()
        datalist.addAll(spinnerList.distinctBy { it.id }) // Avoid duplicate items

        recyclerAdapter = SpinnerRecyclerAdapter(context, datalist, object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                selectedItemPosition = position
                selectedItem = selectedString
                if (position >= 0 && position < datalist.size) {
                    val selectedItem = datalist[position]
                    if (::onItemSelectListener.isInitialized) {
                        onItemSelectListener.setOnItemSelectListener(selectedItem.id, selectedItem.name)
                    }
                } else {
                    Log.e("SpinnerError", "Invalid position: $position, List size: ${datalist.size}")
                }
            }
        })

        recyclerAdapter.initializeData() // Initialize adapter data
    }



    fun setSpinnerListItems2(spinnerList: ArrayList<SearchableItem>) {
        if (isfromsearch) {
            val searchableItems = ArrayList<SearchableItem>()
            for (item in spinnerList) {
                searchableItems.add(SearchableItem(id = item.id, name = item.name))
            }

            datalist = searchableItems
            recyclerAdapter = SpinnerRecyclerAdapter(context, datalist, object : OnItemSelectListener {
                override fun setOnItemSelectListener(position: Int, selectedString: String) {
                    selectedItemPosition = position
                    selectedItem = selectedString
                    val selectedItem = datalist[position]
                    if (::onItemSelectListener.isInitialized)
                        onItemSelectListener.setOnItemSelectListener(
                            selectedItem.id,
                            selectedItem.name
                        )
                }
            })
        } else {
            val searchableItems = ArrayList<SearchableItem>()
            for (item in spinnerList) {
                searchableItems.add(SearchableItem(id = item.id, name = item.name))
            }

            datalist.clear()
            datalist.addAll(searchableItems)

            // Initialize adapter while preserving previous logic
            recyclerAdapter = SpinnerRecyclerAdapter(context, datalist, object : OnItemSelectListener {
                override fun setOnItemSelectListener(position: Int, selectedString: String) {
                    selectedItemPosition = position
                    selectedItem = selectedString
                    if (position >= 0 && position < searchableItems.size) {
                        val selectedItem = searchableItems[position]
                        if (::onItemSelectListener.isInitialized)
                            onItemSelectListener.setOnItemSelectListener(
                                selectedItem.id,
                                selectedItem.name
                            )
                    } else {
                        Log.e("SpinnerError", "Invalid position: $position, List size: ${searchableItems.size}")
                    }
                }
            })
        }
    }


    // Private helper methods
    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun dismissDialogOnDestroy() {
        if (getDialogInfo(false))
            dialog.dismiss()
    }

    private fun initLifeCycleObserver() {
        if (context is AppCompatActivity)
            context.lifecycle.addObserver(this)
        if (context is FragmentActivity)
            context.lifecycle.addObserver(this)
        if (context is Fragment)
            context.lifecycle.addObserver(this)
    }

    private fun getDialogInfo(toShow: Boolean): Boolean {
        return if (toShow) {
            !::dialog.isInitialized || !dialog.isShowing
        } else
            ::dialog.isInitialized && dialog.isShowing && dialog.window != null && dialog.window?.decorView != null && dialog.window?.decorView?.isAttachedToWindow!!
    }

    private fun AlertDialog.initView() {
        setOnShowListener {
            CircularReveal.startReveal(true, this, object : OnAnimationEnd {
                override fun onAnimationEndListener(isRevealed: Boolean) {
                    if (isRevealed) {
                        toggleKeyBoard(true)
                    }
                }
            }, animationDuration)
        }

        if (spinnerCancelable || negativeButtonVisibility != SpinnerView.VISIBLE)
            setOnCancelListener {
                if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(
                    null
                )
            }

        dialog.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                this.dismiss()
            }
            false
        }

        if (windowTitle != null || windowTitleVisibility.visibility == SearchView.VISIBLE) {
            textViewTitle = findViewById(R.id.textViewTitle)!!
            textViewTitle.visibility = View.VISIBLE
            textViewTitle.text = windowTitle
            textViewTitle.setTextColor(windowTitleTextColor)
        } else textViewTitle.visibility = windowTitleVisibility.visibility






        //init SearchView
        if (searchViewVisibility.visibility == SearchView.VISIBLE) {
            searchview.queryHint = searchQueryHint
            /* searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                 override fun onQueryTextSubmit(query: String?): Boolean {
                     if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(query)
                     return false
                 }

                 override fun onQueryTextChange(newText: String?): Boolean {
                     if (::recyclerAdapter.isInitialized) recyclerAdapter.filter(newText)
                     return false
                 }

             })*/




            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    recyclerAdapter.filter(query) // Apply the filter on submit
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    recyclerAdapter.filter(newText) // Apply the filter on text change
                    return false
                }
            })


        } else searchview.visibility = searchViewVisibility.visibility


        if (negativeButtonVisibility.visibility == SearchView.VISIBLE) {
            buttonClose.setOnClickListener {
                it.isClickable = false
                this.dismiss()
            }
            buttonClose.text = negativeButtonText
            buttonClose.setTextColor(negativeButtonTextColor)
        } else buttonClose.visibility = negativeButtonVisibility.visibility

        if (::recyclerAdapter.isInitialized) {
            recyclerAdapter.highlightSelectedItem = highlightSelectedItem
            recyclerView.adapter = recyclerAdapter
        }

        // DONE Button implementation
        btnSubmit.setOnClickListener {
            // Get the selected items from the adapter
            val selectedItems = recyclerAdapter.getSelectedItems()

            // Process the selected items (e.g., display, pass to another activity, etc.)
            if (selectedItems.isNotEmpty()) {
                for (item in selectedItems) {
                    Log.d("Selected Item", "ID: ${item.id}, Name: ${item.name}")
                }

                // Pass the selected items to the listener or parent activity/fragment
                for (item in selectedItems) {
                    onItemSelectListener.setOnItemSelectListener(item.id, item.name)
                }

                dismiss()  // Optionally dismiss the dialog
            } else {
                Toast.makeText(context, "No items selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initDialogColorScheme() {
        if (windowTopBackgroundColor != null)
            headLayouts.background = ColorDrawable(windowTopBackgroundColor!!)
        if (negativeButtonBackgroundColor != null)
            buttonClose.backgroundTintList =
                ColorStateList.valueOf(negativeButtonBackgroundColor!!)
    }

    private fun AlertDialog.initAlertDialogWindow() {
        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
        val insetBackgroundDrawable = InsetDrawable(colorDrawable, 50, 40, 50, 40)
        window?.setBackgroundDrawable(insetBackgroundDrawable)
        window?.attributes?.layoutAnimationParameters
        window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun toggleKeyBoard(showKeyBoard: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (showKeyboardByDefault && showKeyBoard) {
            searchview.post {
                searchview.requestFocus()
                imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        } else {
            imm?.toggleSoftInput(0, 0)
        }
    }
}

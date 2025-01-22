package com.smorzhok.hotelsapp.ui.hotelScreen

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smorzhok.hotelsapp.R
import com.smorzhok.hotelsapp.remote.HotelsAdapter
import com.smorzhok.hotelsapp.ui.detailScreen.DetailScreenActivity


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: HotelsScreenViewModel
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var adapter: HotelsAdapter
    private var positionOfSpinner = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = ViewModelProvider(this, HotelsScreenViewModelFactory(application))
            .get(HotelsScreenViewModel::class.java)

        adapter = HotelsAdapter { hotel ->
            val intent = DetailScreenActivity.newIntent(this, hotel)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        button.setOnClickListener {
            if (positionOfSpinner == 0)
                viewModel.sortByDistance()
            else viewModel.sortByRoom()
        }

        viewModel.hotels.observe(this) { hotels ->
            adapter.setHotels(hotels)
            adapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBarLoading.visibility = View.VISIBLE
            } else {
                progressBarLoading.visibility = View.GONE
            }
        }
        if (viewModel.hotels.value.isNullOrEmpty()) {
            viewModel.loadHotels()
        }
        setupSpinner()
    }

    private fun setupSpinner() {
        spinner.setSelection(-1, false)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    positionOfSpinner = 0
                } else {
                    positionOfSpinner = position
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_position", positionOfSpinner)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        positionOfSpinner = savedInstanceState.getInt("spinner_position", 0)
        spinner.setSelection(positionOfSpinner)
    }

    private fun initViews() {
        spinner = findViewById(R.id.spinner)
        recyclerView = findViewById(R.id.recyclerViewHotels)
        button = findViewById(R.id.sortButton)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBarLoading = findViewById(R.id.progressBarLoading)
    }
}
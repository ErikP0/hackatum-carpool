package de.hackatum2018.sixtcarpool.activities


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView

import de.hackatum2018.sixtcarpool.R
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.subscribeInLifecycle
import de.hackatum2018.sixtcarpool.utils.TextChangedListener
import de.hackatum2018.sixtcarpool.viewmodels.SearchCarpoolViewModel
import kotlinx.android.synthetic.main.fragment_search_carpool.view.*
import java.text.DateFormat
import java.util.*


class SearchCarpoolFragment : Fragment() {

    private lateinit var viewmodel: SearchCarpoolViewModel

    private lateinit var walkRadiusInfo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository.getInstance(AppDatabase.getInstance(context!!))
        val factory = SearchCarpoolViewModel.getFactory(repository)
        viewmodel = ViewModelProviders.of(this, factory).get(SearchCarpoolViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search_carpool, container, false)

        val fromEditText = rootView.search_carpool_form_from
        val toEditText = rootView.search_carpool_form_to
        val startTime = rootView.search_carpool_form_when
        val walkRadiusSeeker = rootView.search_carpool_seek_bar
        walkRadiusInfo = rootView.search_carpool_seek_bar_info
        val searchButton = rootView.search_carpool_search_btn

        fromEditText.addTextChangedListener(TextChangedListener {
            viewmodel.from = it?.toString() ?: ""
        })
        toEditText.addTextChangedListener(TextChangedListener {
            viewmodel.to = it?.toString() ?: ""
        })
        startTime.setOnClickListener { datePicker(startTime) }

        walkRadiusSeeker.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setWalkRadius(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        //init info
        setWalkRadius(walkRadiusSeeker.progress)

        searchButton.setOnClickListener {
            val intent = Intent(activity, SearchCarpoolResultActivity::class.java)
            intent.putExtra(SearchCarpoolResultActivity.WALKING_RADIUS, walkRadiusSeeker.progress)
            startActivity(intent)
        }
        //only enable the search button if input is valid
        viewmodel.enableSearchButton.subscribeInLifecycle(this, onNext = {
            searchButton.isEnabled = it
        })

        return rootView
    }

    private fun setWalkRadius(radiusInMinutes: Int) {
        walkRadiusInfo.text = "max. $radiusInMinutes minutes walk"
    }

    private fun datePicker(textView: TextView) {


        // Get Current Date

        val c = Calendar.getInstance()
        if (viewmodel.startDateTime > System.currentTimeMillis() / 1000) {
            c.timeInMillis = viewmodel.startDateTime * 1000
        }
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                timePicker(textView, y, m, d)
            }, y, m, d
        )
        datePickerDialog.show()
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    private fun timePicker(textView: TextView, year: Int, month: Int, day: Int) {
        // Get Current Time
        val c = Calendar.getInstance()
        val h = c.get(Calendar.HOUR_OF_DAY)
        val m = c.get(Calendar.MINUTE)

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                c.set(year, month, day, hourOfDay, minute)
                val unixTime = c.timeInMillis / 1000
                viewmodel.startDateTime = unixTime
                val formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                formatter.calendar = c
                textView.text = formatter.format(c.time)

            }, h, m, true
        )
        timePickerDialog.show()
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            SearchCarpoolFragment()
    }
}

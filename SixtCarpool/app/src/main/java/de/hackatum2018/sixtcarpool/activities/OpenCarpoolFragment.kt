package de.hackatum2018.sixtcarpool.activities


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import de.hackatum2018.sixtcarpool.R
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.subscribeInLifecycle
import de.hackatum2018.sixtcarpool.utils.TextChangedListener
import de.hackatum2018.sixtcarpool.viewmodels.OpenCarpoolViewModel
import kotlinx.android.synthetic.main.fragment_open_carpool.view.*
import java.text.DateFormat
import java.util.*


class OpenCarpoolFragment : Fragment() {

    private lateinit var viewmodel: OpenCarpoolViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = arguments?.getInt(OpenCarpoolActivity.RENTAL_ID)
            ?: throw IllegalArgumentException("Expected param ${OpenCarpoolActivity.RENTAL_ID}")
        val database = AppDatabase.getInstance(context!!)
        val repository = Repository.getInstance(database)
        val factory = OpenCarpoolViewModel.getFactory(id, repository)
        viewmodel = ViewModelProviders.of(this, factory).get(OpenCarpoolViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_open_carpool, container, false)

        //prefill once
        val fromEdit = rootView.open_carpool_form_from
        val toEdit = rootView.open_carpool_form_to
        val timeEdit = rootView.open_carpool_form_when
        val maxPassengerSpinner = rootView.max_passenger_spinner
        val priceEdit = rootView.open_carpool_form_price

        viewmodel.rental.firstOrError().subscribeInLifecycle(this, onSuccess = { rental ->
            if (rental == null) throw IllegalArgumentException("Rental is null")
            fromEdit.append(rental.pickupStationName)
            toEdit.append(rental.returnStationName)

            val values = (1 until rental.maxPassengers).toList().map(Int::toString).toTypedArray()
            maxPassengerSpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, values)

            maxPassengerSpinner.setSelection(values.size - 1)//default to most passengers
        })

        fromEdit.addTextChangedListener(TextChangedListener {
            viewmodel.from = it?.toString() ?: ""
        })
        toEdit.addTextChangedListener(TextChangedListener {
            viewmodel.to = it?.toString() ?: ""
        })
        maxPassengerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewmodel.maxPassengers = position + 1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
        priceEdit.addTextChangedListener(TextChangedListener {
            viewmodel.price = it?.toString()?.toInt() ?: 0
        })

        timeEdit.setOnClickListener {
            //open time and date picker dialog
            datePicker(timeEdit)
        }

        //enable/disable button
        val button = rootView.open_carpool_btn
        viewmodel.buttonEnabled.subscribeInLifecycle(this, onNext = { button.isEnabled = it })
        button.setOnClickListener {
            viewmodel.openToCarpool().subscribeInLifecycle(this, onComplete = {
                activity?.finish()
            })
        }

        return rootView
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
            }, y, m, d)
        datePickerDialog.show()
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() -1000
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
        fun newInstance(id: Int) =
            OpenCarpoolFragment().apply {
                arguments = Bundle().apply {
                    putInt(OpenCarpoolActivity.RENTAL_ID, id)
                }

            }
    }
}


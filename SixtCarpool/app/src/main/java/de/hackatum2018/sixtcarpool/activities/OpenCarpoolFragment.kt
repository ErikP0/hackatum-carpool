package de.hackatum2018.sixtcarpool.activities


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import de.hackatum2018.sixtcarpool.R
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.subscribeInLifecycle
import de.hackatum2018.sixtcarpool.viewmodels.OpenCarpoolViewModel
import kotlinx.android.synthetic.main.fragment_open_carpool.view.*


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
        timeEdit.addTextChangedListener(TextChangedListener {
            viewmodel.startDateTime = it?.toString()?.toLong() ?: 0
        })
        maxPassengerSpinner.setOnItemClickListener { _, _, position, _ ->
            viewmodel.maxPassengers = position
        }
        priceEdit.addTextChangedListener(TextChangedListener {
            viewmodel.price = it?.toString()?.toInt() ?: 0
        })

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

private class TextChangedListener(private val onTextChangedListener: (Editable?) -> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        onTextChangedListener(p0)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}

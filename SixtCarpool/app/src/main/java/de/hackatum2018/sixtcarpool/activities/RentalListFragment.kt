package de.hackatum2018.sixtcarpool.activities


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hackatum2018.sixtcarpool.R
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.subscribeInLifecycle
import de.hackatum2018.sixtcarpool.utils.ItemListAdapter
import de.hackatum2018.sixtcarpool.viewmodels.RentalListViewModel
import kotlinx.android.synthetic.main.fragment_rental_list.view.*
import kotlinx.android.synthetic.main.rental_car_list_content.view.*


class RentalListFragment : Fragment() {

    private lateinit var viewmodel: RentalListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository.getInstance(AppDatabase.getInstance(context!!))
        val factory = RentalListViewModel.getFactory(repository)
        viewmodel = ViewModelProviders.of(this, factory).get(RentalListViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_rental_list, container, false)
        val list = rootView.rental_car_list
        val adapter = ItemListAdapter<CarRental, MyCarRentalViewHolder>(
            context = context!!,
            layoutId = R.layout.rental_car_list_content,
            viewHolderProvider = ::MyCarRentalViewHolder,
            bindView = { myCarRental, _ ->
                nameText.text = myCarRental.name
                if (myCarRental.imageUrl.isNotEmpty()) {
                    Picasso.get().load(Uri.parse(myCarRental.imageUrl))
                        .fit()
                        .centerInside()
                        .into(imageView)
                } else {
                    imageView.setImageResource(0)
                }

                beginDateText.text =
                        DateUtils.formatDateTime(context, myCarRental.pickupTime, DateUtils.FORMAT_ABBREV_RELATIVE)
                endDateText.text =
                        DateUtils.formatDateTime(context, myCarRental.returnTime, DateUtils.FORMAT_ABBREV_RELATIVE)
                pickupLocationText.text = myCarRental.pickupStationName
                returnLocationText.text = myCarRental.returnStationName

                card.setOnClickListener {
                    val intent = Intent(activity, OpenCarpoolActivity::class.java)
                    intent.putExtra(OpenCarpoolActivity.RENTAL_ID, myCarRental.id)
                    startActivity(intent)
                }

                carpooledCheck.visibility = if (myCarRental.isOffered) View.VISIBLE else View.GONE
            }
        )
        list.adapter = adapter

        viewmodel.rentalList.subscribeInLifecycle(this, onNext = { myRentalList ->
            adapter.items = myRentalList
        })

        return rootView
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            RentalListFragment()
    }
}

private class MyCarRentalViewHolder(val card: View) : RecyclerView.ViewHolder(card) {
    val nameText: TextView = card.car_list_content_name
    val imageView = card.car_list_content_image
    val beginDateText = card.car_list_content_begin_date
    val endDateText = card.car_list_content_end_date
    val pickupLocationText = card.car_list_content_pickup
    val returnLocationText = card.car_list_content_return
    val carpooledCheck = card.car_list_content_carpool_check
}

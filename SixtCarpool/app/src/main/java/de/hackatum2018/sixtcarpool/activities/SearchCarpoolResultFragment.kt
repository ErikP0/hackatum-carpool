package de.hackatum2018.sixtcarpool.activities


import android.arch.lifecycle.ViewModelProviders
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
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import de.hackatum2018.sixtcarpool.subscribeInLifecycle
import de.hackatum2018.sixtcarpool.utils.ItemListAdapter
import de.hackatum2018.sixtcarpool.viewmodels.SearchCarpoolResultViewModel
import kotlinx.android.synthetic.main.carpool_offer_list_content.view.*
import kotlinx.android.synthetic.main.fragment_search_carpool_result.view.*

typealias CarpoolOfferDetail = Pair<CarpoolOffer, CarRental>

class SearchCarpoolResultFragment : Fragment() {
    private var radius: Int = 5
    private lateinit var viewmodel: SearchCarpoolResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            radius = it.getInt(SearchCarpoolResultActivity.WALKING_RADIUS)
        }
        val repository = Repository.getInstance(AppDatabase.getInstance(context!!))
        val factory = SearchCarpoolResultViewModel.getFactory(repository)
        viewmodel = ViewModelProviders.of(this, factory).get(SearchCarpoolResultViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search_carpool_result, container, false)
        val list = rootView.carpool_offer_list
        val adapter = ItemListAdapter<CarpoolOfferDetail, CarpoolOfferDetailViewHolder>(
            context = context!!,
            layoutId = R.layout.carpool_offer_list_content,
            viewHolderProvider = ::CarpoolOfferDetailViewHolder,
            bindView = { (offer, rental), _ ->
                nameText.text = rental.name
                if (rental.imageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(rental.imageUrl)
                        .fit()
                        .centerInside()
                        .into(imageView)
                } else {
                    imageView.setImageResource(0)
                }
                beginDateText.text =
                        DateUtils.formatDateTime(context, rental.pickupTime, DateUtils.FORMAT_ABBREV_RELATIVE)
                pickupLocationText.text = offer.placeFrom
                returnLocationText.text = offer.placeTo
                passengerText.text = "/${offer.maxPassengers}"
                priceText.text = "${offer.pricePerPassenger}€"
            }
        )
        list.adapter = adapter

        val progressBar = rootView.carpool_offer_progress

//        val progressBar = ProgressBar(context)
//        progressBar.isIndeterminate = true
//        val waitDialog = AlertDialog.Builder(context)
//            .setTitle("Searching...")
//            .setView(progressBar)
//            .setNegativeButton(android.R.string.cancel, null)
//            .show()
        viewmodel.search(radius).subscribeInLifecycle(this, onSuccess = {
            progressBar.visibility = View.GONE
            adapter.items = it
        })
        return rootView
    }


    companion object {


        @JvmStatic
        fun newInstance(walkRadius: Int) =
            SearchCarpoolResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(SearchCarpoolResultActivity.WALKING_RADIUS, walkRadius)
                }
            }
    }
}

private class CarpoolOfferDetailViewHolder(val card: View) : RecyclerView.ViewHolder(card) {
    val nameText: TextView = card.carpool_offer_list_name
    val imageView = card.carpool_offer_list_model_image
    val beginDateText = card.carpool_offer_list_begin_datetime
    val pickupLocationText = card.carpool_offer_list_start
    val returnLocationText = card.carpool_offer_list_end
    val passengerText = card.carpool_offer_list_passenger_info
    val priceText = card.carpool_offer_list_price
}
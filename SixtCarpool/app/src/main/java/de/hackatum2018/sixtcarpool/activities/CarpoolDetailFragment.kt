package de.hackatum2018.sixtcarpool.activities


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hackatum2018.sixtcarpool.R
import kotlinx.android.synthetic.main.fragment_carpool_detail.view.*
import net.sectorsieteg.avatars.AvatarDrawableFactory


class CarpoolDetailFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getInt(CarpoolDetailActivity.OFFER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_carpool_detail, container, false)
        val avatarFactory = AvatarDrawableFactory(resources)

        val options = BitmapFactory.Options()
        options.inMutable = false
        val avatar = BitmapFactory.decodeResource(resources, R.drawable.passenger2, options)
        val avatarDrawable = avatarFactory.getRoundedAvatarDrawable(avatar)
        val avatarView = rootView.carpool_detail_avatar
        avatarView.setImageDrawable(avatarDrawable)

        val button = rootView.carpool_detail_send_offer_btn
        button.setOnClickListener { activity?.finish() }
        return rootView
    }


    companion object {
        @JvmStatic
        fun newInstance(offerId: Int) =
            CarpoolDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(CarpoolDetailActivity.OFFER_ID, offerId)
                }
            }
    }
}

package de.hackatum2018.sixtcarpool.activities


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.hackatum2018.sixtcarpool.R


class OpenCarpoolFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_carpool, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            OpenCarpoolFragment()
    }
}

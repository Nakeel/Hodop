package com.we2dx.hodop.ui.traffic_updates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.we2dx.hodop.R

class TrafficUpdatesFragment : Fragment() {

    private lateinit var trafficUpdatesViewModel: TrafficUpdatesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trafficUpdatesViewModel =
            ViewModelProviders.of(this).get(TrafficUpdatesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_traffic_updates, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        trafficUpdatesViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
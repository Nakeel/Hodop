package com.we2dx.hodop.ui.aboutapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.we2dx.hodop.R

class AboutAppFragment : Fragment() {

    private lateinit var aboutAppViewModel: AboutAppViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutAppViewModel =
            ViewModelProviders.of(this).get(AboutAppViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_about_app, container, false)
        val textView: TextView = root.findViewById(R.id.text_tools)
        aboutAppViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
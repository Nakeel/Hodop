package com.we2dx.hodop.ui.newsletter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.we2dx.hodop.R

class SubscribeNewsletterFragment : Fragment() {

    private lateinit var subcribeNewsletterViewModel: SubcribeNewsletterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subcribeNewsletterViewModel =
            ViewModelProviders.of(this).get(SubcribeNewsletterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_newsletter, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        subcribeNewsletterViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
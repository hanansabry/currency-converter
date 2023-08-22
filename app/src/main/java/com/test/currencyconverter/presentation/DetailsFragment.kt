package com.test.currencyconverter.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.currencyconverter.R
import com.test.currencyconverter.SharedPreferencesManager
import com.test.currencyconverter.databinding.FragmentConversionBinding
import com.test.currencyconverter.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var sharedPreferences: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences = SharedPreferencesManager(requireContext())
        binding = FragmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val history = sharedPreferences.getHistoryByDate(System.currentTimeMillis())
        val adapter = HistoryAdapter(history)
        binding.adapter = adapter
        return binding.root
    }

}
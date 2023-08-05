package com.test.currencyconverter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.currencyconverter.databinding.FragmentConversionBinding


class ConversionFragment : Fragment() {

    private lateinit var binding: FragmentConversionBinding
    private val viewModel: SymbolsViewModel by lazy {
        ViewModelProvider(this)[SymbolsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragment = this

        viewModel.symbols.observe(this) {
            val list = ArrayList<String>()
            it?.forEach { (key, value) ->
                println("$key: $value")
                list.add(key)
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
            viewModel.setSymbolsAdapter(adapter)
        }

        viewModel.error.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        return binding.root
    }

    fun onSwitchSymbolsClicked(v: View) {
        val selectedFrom = binding.from.selectedItemPosition
        val selectedTo = binding.to.selectedItemPosition
        viewModel.setSelectedFromItem(selectedTo)
        viewModel.setSelectedToItem(selectedFrom)
    }
}
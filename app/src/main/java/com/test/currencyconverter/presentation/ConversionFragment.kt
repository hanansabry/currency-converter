package com.test.currencyconverter.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.currencyconverter.databinding.FragmentConversionBinding


class ConversionFragment : Fragment() {

    private var isSwitching: Boolean = false
    private lateinit var adapter: ArrayAdapter<String>
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

        binding.from.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                if (!isSwitching) {
                    viewModel.setEmitDirectly(true)
                    viewModel.setSelectedFromItem(position)
                    Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
                // This method is called when the selection is cleared.
            }
        }

        binding.to.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                if (!isSwitching) {
                    viewModel.setEmitDirectly(true)
                    viewModel.setSelectedToItem(position)
                    Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
                // This method is called when the selection is cleared.
            }
        }

        binding.fromValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    val from = adapter.getItem(viewModel.selectedFromItem.value!!)
                    val to = adapter.getItem(viewModel.selectedToItem.value!!)
                    viewModel.convert(
                        "$from,$to"
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
                // You can access the final text using the 's' parameter.
            }
        })

        viewModel.symbols.observe(this) {
            val list = ArrayList<String>()
            it?.forEach { (key, value) ->
                println("$key: $value")
                list.add(key)
            }
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
            viewModel.setSymbolsAdapter(adapter)
            viewModel.setEmitDirectly(false)
            isSwitching = true
            viewModel.setSelectedToItem(43)
            viewModel.setSelectedFromItem(0)
        }

        viewModel.convertResult.observe(this) {
            isSwitching = false
            if (it?.entries?.size!! > 1) {
                it.forEach { (key, value) ->
                    println("$key: $value")
                }
                val fromSymbol = adapter.getItem(viewModel.selectedFromItem.value!!)
                val toSymbol = adapter.getItem(viewModel.selectedToItem.value!!)
                val fromSymbolRate = it[fromSymbol.toString()]
                val toSymbolRate = it[toSymbol.toString()]

                //calculate result
                val fromValueText = binding.fromValue.text.toString()
                val fromValueAsDouble = fromValueText.toDouble()
                val convertRate = (1 / fromSymbolRate!!) * toSymbolRate!!
                val convertResult = fromValueAsDouble * convertRate
                binding.toValue.setText(String.format("%.3f", convertResult))
            } else {
                binding.toValue.setText(binding.fromValue.text.toString())
            }
        }

        //TODO: comment this temporarly to not convert every time
//        viewModel.getCombinedResult().observe(this) {
//            //convert current value
//            val from = adapter.getItem(it.first)
//            val to = adapter.getItem(it.second)
//            viewModel.convert(
//                "$from,$to"
//            )
//        }

        viewModel.error.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        return binding.root
    }

    fun onSwitchSymbolsClicked(v: View) {
        isSwitching = true
        val selectedFrom = binding.from.selectedItemPosition
        val selectedTo = binding.to.selectedItemPosition
//        viewModel.switchSelectedValues(selectedFrom, selectedTo);
        viewModel.setEmitDirectly(false)
        viewModel.setSelectedFromItem(selectedTo)
        viewModel.setSelectedToItem(selectedFrom)
    }

    fun onDetailsClicked(v: View) {
        this.findNavController().navigate(ConversionFragmentDirections.actionConversionFragmentToDetailsFragment());
    }
}
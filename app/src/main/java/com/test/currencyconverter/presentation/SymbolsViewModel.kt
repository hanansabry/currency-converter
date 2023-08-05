package com.test.currencyconverter.presentation

import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyconverter.BuildConfig
import com.test.currencyconverter.network.FixerApi
import com.test.currencyconverter.network.SymbolResponse
import kotlinx.coroutines.launch

class SymbolsViewModel : ViewModel() {

    private val _symbols = MutableLiveData<Map<String, String>?>()
    val symbols: MutableLiveData<Map<String, String>?>
        get() = _symbols

    private val _symbolResponse = MutableLiveData<SymbolResponse>()

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?>
        get() = _error

    private val _symbolsAdapter = MutableLiveData<ArrayAdapter<String>>()
    val symbolsAdapter: LiveData<ArrayAdapter<String>>
        get() = _symbolsAdapter

    private val _selectedFromItem = MutableLiveData<Int>()
    val selectedFromItem: LiveData<Int>
        get() = _selectedFromItem

    private val _selectedToItem = MutableLiveData<Int>()
    val selectedToItem: LiveData<Int>
        get() = _selectedToItem

    init {
        getSymbolProperties()
    }

    private fun getSymbolProperties() {
        viewModelScope.launch {
            try {
                _symbolResponse.value = FixerApi.retrofitService.getSymbols(BuildConfig.API_KEY)
                _symbolResponse.value?.let { response ->
                    _symbols.value = response.symbols
                    response.error?.info?.let {
                        _error.value = it
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun setSymbolsAdapter(adapter: ArrayAdapter<String>) {
        _symbolsAdapter.value = adapter
    }

    fun setSelectedFromItem(position: Int) {
        _selectedFromItem.value = position
    }

    fun setSelectedToItem(position: Int) {
        _selectedToItem.value = position
    }
}
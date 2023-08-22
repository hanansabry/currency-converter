package com.test.currencyconverter.presentation

import android.widget.ArrayAdapter
import androidx.lifecycle.*
import com.test.currencyconverter.BuildConfig
import com.test.currencyconverter.network.FixerApi
import kotlinx.coroutines.launch

class SymbolsViewModel : ViewModel() {

    private val _symbols = MutableLiveData<Map<String, String>?>()
    val symbols: LiveData<Map<String, String>?>
        get() = _symbols

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
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

    private val _convertResult = MutableLiveData<Map<String, Double>?>()
    val convertResult: LiveData<Map<String, Double>?>
        get() = _convertResult

    private var isForEmitted = false
    private var isToEmitted = false
    private var emitDirectly = false;

    // Expose LiveData to observe the combined results
    private val combinedResultLiveData = MediatorLiveData<Pair<Int, Int>>()

    private fun combineLatestObservables() {
        combinedResultLiveData.addSource(selectedFromItem) { from ->
            val to = selectedToItem.value
            if ((emitDirectly && to != null) || (!isForEmitted && !isToEmitted && to != null)) {
                isForEmitted = true
                isToEmitted = true
                combinedResultLiveData.value = Pair(from, to)
            }
        }

        combinedResultLiveData.addSource(selectedToItem) { to ->
            val from = selectedFromItem.value
            if ((emitDirectly && from != null)|| (!isForEmitted && !isToEmitted && from != null)) {
                isToEmitted = true
                isForEmitted = true
                combinedResultLiveData.value = Pair(from, to)
            }
        }
    }

    fun getCombinedResult(): LiveData<Pair<Int, Int>> {
        return combinedResultLiveData
    }

    init {
        getSymbolProperties()
        combineLatestObservables()
    }

    private fun getSymbolProperties() {
        viewModelScope.launch {
            try {
                val symbolResponse = FixerApi.retrofitService.getSymbols(BuildConfig.API_KEY)
                symbolResponse.let { response ->
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

    fun convert(symbols: String) {
        viewModelScope.launch {
            try {
                val convertResponse =
                    FixerApi.retrofitService.getConvertRate(BuildConfig.API_KEY, symbols)
                _convertResult.value = convertResponse.rates
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun setSymbolsAdapter(adapter: ArrayAdapter<String>) {
        _symbolsAdapter.value = adapter
    }

    fun setSelectedFromItem(position: Int) {
        isForEmitted = false
        _selectedFromItem.value = position
    }

    fun setSelectedToItem(position: Int) {
        isToEmitted = false
        _selectedToItem.value = position
    }

    fun setEmitDirectly(emit: Boolean) {
        emitDirectly = emit
    }

    fun switchSelectedValues(selectedFrom: Int, selectedTo: Int) {
        _selectedToItem.value = selectedFrom
        _selectedFromItem.value = selectedTo
    }

}
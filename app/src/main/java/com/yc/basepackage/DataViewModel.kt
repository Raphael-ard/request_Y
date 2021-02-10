package com.yc.basepackage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel(DataStr: String) : ViewModel() {
    private val da_str = MutableLiveData<String>()
    val str: LiveData<String>
    get() = da_str
    init {
        da_str.value = DataStr
    }
}
package com.example.jatpackdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel的最佳写法
 */
class MainViewModel(countReserved: Int) : ViewModel() {

    // 外部暴露不可变
    val counter : LiveData<Int>
        get() = _counter

    // 内部使用可变的
    private val _counter = MutableLiveData<Int>()
    init {
        _counter.value = countReserved
    }

    fun plusOne() {
        val count = _counter.value ?: 0
        _counter.value = count + 1
    }

    fun clear() {
        _counter.value = 0
    }
}
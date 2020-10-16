package com.example.jatpackdemo

import androidx.lifecycle.ViewModel

class MainViewModel(countReserved: Int) : ViewModel() {
    var count = countReserved
}
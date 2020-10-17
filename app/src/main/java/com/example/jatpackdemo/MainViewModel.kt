package com.example.jatpackdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.jatpackdemo.data.Repository
import com.example.jatpackdemo.model.User

/**
 * ViewModel的最佳写法
 */
class MainViewModel(countReserved: Int) : ViewModel() {

    // map()
    private val userLiveData = MutableLiveData<User>()
    var userName: LiveData<String> = Transformations.map(userLiveData) { user ->
        "${user.firstName} ${user.lastName}"
    }

    // switchMap()
    private val userIdLiveData = MutableLiveData<String>()
    val user: LiveData<User> = Transformations.switchMap(userIdLiveData) { userId ->
        Repository.getUser(userId)
    }
    fun setUserId(userId: String) {
        userIdLiveData.value = userId
    }

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
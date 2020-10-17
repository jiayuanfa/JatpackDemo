package com.example.jatpackdemo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.jatpackdemo.lifecycle.MyObserver
import com.example.jatpackdemo.model.*
import com.example.jatpackdemo.workmanager.SimpleWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    val user1 = User("Tom", "Brady", 24)
    val user2 = User("Tom", "Hanks", 63)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 数据库相关
        val userDao = AppDatabase.getDatabase(this).userDao()
        val bookDao = AppDatabase.getDatabase(this).bookDao()

        // 添加lifecycle实例
        lifecycle.addObserver(MyObserver(lifecycle))

        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        viewModel = ViewModelProvider(this, MainViewModelFactory(countReserved)).get(MainViewModel::class.java)
        plusButton.setOnClickListener {
            viewModel.plusOne()
        }
        clearButton.setOnClickListener {
            viewModel.clear()
        }
        viewModel.counter.observe(this, Observer {count ->
            countTv.text = count.toString()
        })
        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            viewModel.setUserId(userId)
        }
        viewModel.user.observe(this, Observer { user ->
            countTv.text = user.firstName
        })

        handleDatabase(userDao)
        handleBookDatabase(bookDao)
        doWork()
    }

    private fun doWork() {
        doWorkBtn.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }

    private fun handleBookDatabase(bookDao: BookDao) {
        insertBookDao.setOnClickListener {
            thread {
                val book1 = Book("三国演义", 345, "施耐庵")
                bookDao.insertBook(book1)
            }
        }
        queryBookDao.setOnClickListener {
            thread {
                for (book in bookDao.loadAllBooks()) {
                    Log.d("Book data is", book.toString())
                }
            }
        }
    }

    /**
     * 操作数据库
     */
    private fun handleDatabase(userDao: UserDao) {

        addButton.setOnClickListener {
            thread {
                user1.id = userDao.insertUser(user1)
                user2.id = userDao.insertUser(user2)
            }
        }
        deleteButton.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Hanks")
            }
        }
        updateButton.setOnClickListener {
            thread {
                user1.age = 43
                userDao.updateUser(user1)
            }
        }
        queryButton.setOnClickListener {
            thread {
                for (user in userDao.loadAllUsers()) {
                    Log.d("MainActivity", user.toString())
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }
}
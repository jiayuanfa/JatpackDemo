package com.example.jatpackdemo.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * WorkManager
 * 1: 周期执行任务
 * 2：doWork任务不会运行在主线程中，所以可以放心的执行耗时任务
 * 3: 可以用，但是不要指望在国产手机上能够稳定运行
 */
class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        Log.d("SimpleWorker", "do work in SimpleWorker")
        return Result.success()
    }
}
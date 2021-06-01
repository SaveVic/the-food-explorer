package com.example.thefoodexplorer.util

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor

class AppExecutors @VisibleForTesting constructor(
    private val diskIO : Executor,
    private val networkIO : Executor,
    private val mainThread: Executor
) {

    companion object{
        private const val THREAD_COUNT = 3
    }

    fun diskIO(): Executor = diskIO

    fun networkIO(): Executor = networkIO

    fun mainThread(): Executor = mainThread

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
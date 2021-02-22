package com.kanyideveloper.workmanagerdemo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DownloadingWorker(context: Context, workerParameters: WorkerParameters)
    : Worker(context,workerParameters) {

    @SuppressLint("SimpleDateFormat")
    override fun doWork(): Result {

        try {
            for(i in 0..3000){
                Log.d("WorkerTag","Downloading $i")
            }

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            //This tag will be displayed after every 16 minutes
            Log.d("Downloading completed", currentDate)

            return Result.success()

        }catch (e: Exception){
            return Result.failure()
        }
    }
}
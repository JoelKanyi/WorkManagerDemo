package com.kanyideveloper.workmanagerdemo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(context: Context, workerParameters: WorkerParameters)
    : Worker(context,workerParameters) {

    companion object{
        const val KEY_WORKER = "key_worker"
    }

    @SuppressLint("SimpleDateFormat")
    override fun doWork(): Result {
        try {
            val count = inputData.getInt(MainActivity.KEY_COUNT_VALUE,0)
            for(i in 0 until count){
                Log.d("WorkerTag","Uploading $i")
            }

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            //passing data from the worker to the activity --- we need to pass the data as parameter in the Result.success method
            val outputData = Data.Builder()
                .putString(KEY_WORKER,currentDate)
                .build()

            return Result.success(outputData)

        }catch (e: Exception){
            return Result.failure()
        }
    }
}
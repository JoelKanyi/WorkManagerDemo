package com.kanyideveloper.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class CompressingWorker(context: Context, workerParameters: WorkerParameters)
    : Worker(context,workerParameters) {

    override fun doWork(): Result {

        try {
            for(i in 0..300){
                Log.d("WorkerTag","Compressing $i")
            }

            return Result.success()

        }catch (e: Exception){
            return Result.failure()
        }
    }
}
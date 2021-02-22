package com.kanyideveloper.workmanagerdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.kanyideveloper.workmanagerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object{
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            //setOneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)

        //Passing data to the worker
        val data: Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE,200)
            .build()

        //setting constraints for our worker
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //an object of our worker class
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val filterWorker = OneTimeWorkRequest.Builder(FilterWorker::class.java).build()
        val compressingWorker = OneTimeWorkRequest.Builder(CompressingWorker::class.java).build()
        val downloadingWorker = OneTimeWorkRequest.Builder(DownloadingWorker::class.java).build()

        //adding the worker request to the execution queue
        //workManager.enqueue(uploadRequest)

        //For Parallel execution
        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(downloadingWorker)
        parallelWorks.add(filterWorker)

        //chaining requests
        workManager
            .beginWith(parallelWorks)
            .then(compressingWorker)
            .then(uploadRequest)
            .enqueue()

        //observing the state of our request
        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, Observer {
            binding.textView.text = it.state.name

            //if the work is finished
            if (it.state.isFinished){
                val outputData = it.outputData
                val message = outputData.getString(UploadWorker.KEY_WORKER)
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }
        })
    }

    //Work that need to repeat after a period of time
    private fun setPeriodicWorkRequest(){
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadingWorker::class.java,16,TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}
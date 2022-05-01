package com.izmir.izmirli.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.izmir.izmirli.model.Location
import com.izmir.izmirli.model.NobetciEczaneResponse
import com.kafein.weatherapp.BaseViewModel
import com.kafein.weatherapp.IzmirAPIService
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class IzmirViewModel(application: Application) : BaseViewModel(application) {

    private var izmirAPIService = IzmirAPIService() // Servisimizi tanımladık.
    var eczaneListLiveData: MutableLiveData<NobetciEczaneResponse> = MutableLiveData() //Livedatamı tanımladım.
    var errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getEczaneList(){
        izmirAPIService.getNobetciEczane()
            .subscribeOn(Schedulers.newThread())
            .subscribeWith(object : DisposableSingleObserver<NobetciEczaneResponse>() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onSuccess(eczaneler: NobetciEczaneResponse) {
                    eczaneListLiveData.postValue(eczaneler)
                }
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    errorLiveData.postValue(e.message.toString())
                }
            })
    }


}
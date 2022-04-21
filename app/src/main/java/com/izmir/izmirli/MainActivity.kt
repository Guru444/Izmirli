package com.izmir.izmirli

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.izmir.izmirli.adapter.EczaneNameAdapter
import com.izmir.izmirli.adapter.IzmirAdapter
import com.izmir.izmirli.databinding.ActivityMainBinding
import com.izmir.izmirli.model.GameTypeResponse
import com.izmir.izmirli.model.NobetciEczaneResponse
import com.izmir.izmirli.model.TrenGarlariResponse
import com.izmir.izmirli.util.*
import com.kafein.weatherapp.IzmirAPIService
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.Handler
import android.view.MotionEvent

import android.view.View.OnTouchListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.izmir.izmirli.model.Location


class MainActivity : AppCompatActivity(), Example {

    private val izmirAdapter = IzmirAdapter()
    private val eczaneNameAdapter = EczaneNameAdapter()
    private var eczaneNameList: ArrayList<String?> = arrayListOf()
    private var eczaneList: ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem> = arrayListOf()
    private var locationList: ArrayList<Location> = arrayListOf()
    private lateinit var binding: ActivityMainBinding
    private lateinit var exampleInterface: Example
    private var izmirAPIService = IzmirAPIService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pbLoader.visibility = View.VISIBLE
        exampleInterface = this

        binding.apply {
        categoryList().forEach {
            tbCategory.addTab(tbCategory.newTab().setText(it))
        }

        swRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                izmirAdapter.setIzmirData(eczaneList)
                swRefresh.isRefreshing = false
                searchInput.text?.clear()
            }
        })

        izmirAPIService.getNobetciEczane()
            .subscribeOn(Schedulers.newThread())
            .subscribeWith(object : DisposableSingleObserver<NobetciEczaneResponse>() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onSuccess(eczaneler: NobetciEczaneResponse) {
                    runOnUiThread {
                        eczaneler[0].tarih?.replace("T08:00:00", " ")?.trim()?.let { Log.i("Tarih", it) }
                        //filter fun -> .filter { it.bolgeId == 19 } as ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem>
                        izmirAdapter.setIzmirData(eczaneler)
                        eczaneler.forEach {
                            it.let {
                                eczaneNameList.add(it.adi)
                                var location = Location().apply {
                                    lat = it.lokasyonX?.toDouble()
                                    long = it.lokasyonY?.toDouble()
                                    name = it.adi
                                }
                                locationList.add(location)
                            }
                        }

                        eczaneList.addAll(eczaneler)
                        Log.i("response", eczaneler.toString())
                        pbLoader.visibility = View.GONE
                    }
                }
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Log.i("response", e.message.toString())
                }
            })

        searchInput.doAfterTextChanged {
            it?.let { search->
                if (search.isNotEmpty()){
                    if (it.length > 3){
                        izmirAdapter.setIzmirData(eczaneList.filter { it.adi?.lowercase()?.startsWith(search.toString().lowercase()) == true } as ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem>)
                    }
                }else{
                    izmirAdapter.clear()
                    izmirAdapter.setIzmirData(eczaneList)
                }
            }
        }
            izmirAdapter.eczaneClickListener = {
                this@MainActivity.eczaneDetay(it, locationList.locationList())
            }

            rvMultitypeList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            rvMultitypeList.adapter = izmirAdapter

            tbCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tbSubCategory.removeAllTabs()
                    when(tab?.position){
                        0 -> { tbSubCategory.autoFillList(subCategoryList()) }
                        1 -> { tbSubCategory.autoFillList(subCategoryList2()) }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            tbSubCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        //0 -> { izmirAdapter.adapterFill(subCategoryList()) }
                       // 1 -> { izmirAdapter.adapterFill(subCategoryList2()) }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

        }
    }

    //Example interface
    override fun listeyiDoldur(liste: ArrayList<String>) {

    }



}
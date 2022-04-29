package com.izmir.izmirli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.izmir.izmirli.adapter.EczaneNameAdapter
import com.izmir.izmirli.adapter.IzmirAdapter
import com.izmir.izmirli.databinding.ActivityMainBinding
import com.izmir.izmirli.model.NobetciEczaneResponse
import com.izmir.izmirli.util.*
import kotlinx.android.synthetic.main.activity_main.*

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.izmir.izmirli.model.Location
import com.izmir.izmirli.viewmodel.IzmirViewModel
import android.content.DialogInterface





class MainActivity : AppCompatActivity(), Example {

    private val izmirAdapter = IzmirAdapter()
    private val eczaneNameAdapter = EczaneNameAdapter()
    private var eczaneNameList: ArrayList<String?> = arrayListOf()
    private var eczaneList: ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem> = arrayListOf()
    private var locationList: ArrayList<Location> = arrayListOf()
    private lateinit var binding: ActivityMainBinding
    private lateinit var exampleInterface: Example
    private lateinit var eczaneViewModel: IzmirViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pbLoader.visibility = View.VISIBLE
        exampleInterface = this

        this.showAlertDialog()


        eczaneViewModel = ViewModelProvider(this).get(IzmirViewModel::class.java)

        eczaneViewModel.getEczaneList()
        observableLiveData()

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

    private fun observableLiveData(){
        eczaneViewModel.eczaneListLiveData.observe(this, {
            it[0].adi?.let { it1 -> this.showMessage(it1) }
            it[0].tarih?.replace("T08:00:00", " ")?.trim()?.let { Log.i("Tarih", it) }
            //filter fun -> .filter { it.bolgeId == 19 } as ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem>
            izmirAdapter.setIzmirData(it)
            it.forEach {
                it.let {
                    eczaneNameList.add(it.adi)
                    val location = Location().apply {
                        lat = it.lokasyonX?.toDouble()
                        long = it.lokasyonY?.toDouble()
                        name = it.adi
                    }
                    locationList.add(location)
                }
            }

            eczaneList.addAll(it)
            Log.i("response", it.toString())
            binding.pbLoader.visibility = View.GONE
        })

        eczaneViewModel.errorLiveData.observe(this,{
            it?.let {
                this.showMessage(it)
            }
        })
    }

    //Example interface
    override fun listeyiDoldur(liste: ArrayList<String>) {

    }




}
package com.izmir.izmirli.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.izmir.izmirli.R
import com.izmir.izmirli.model.Location
import com.izmir.izmirli.model.NobetciEczaneResponse
import com.izmir.izmirli.view.MapsActivity
import kotlinx.android.synthetic.main.result_quick.view.*

fun Activity.eczaneDetay(response: NobetciEczaneResponse.NobetciEczaneResponseItem, locationList: ArrayList<Location>){
    val view: View = this.layoutInflater.inflate(R.layout.result_quick, null)
    val bottomSheetDialog = BottomSheetDialog(this)
    view.rootView.tv_eczane_title.text = getString(R.string.eczane_result_name, response.adi?.lowercase()?.replaceFirstChar { it.uppercase() })
    view.rootView.tv_result.text = response.adres
    view.rootView.btn_open_location.setOnClickListener {
        var intent = Intent(this, MapsActivity::class.java)
        intent.putExtra(İzmirConstant.LOCATION_LAT, response.lokasyonX)
        intent.putExtra(İzmirConstant.LOCATION_LONG, response.lokasyonY)
        intent.putExtra(İzmirConstant.ECZANE_NAME, response.adi)
        intent.putParcelableArrayListExtra(İzmirConstant.LOCATION_LIST, locationList)
        startActivity(intent)
    }
    bottomSheetDialog.setContentView(view)
    bottomSheetDialog.show()
}
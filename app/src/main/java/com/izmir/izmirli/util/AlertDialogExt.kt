package com.izmir.izmirli.util

import android.app.Activity
import androidx.appcompat.app.AlertDialog

fun Activity.showAlertDialog(){
    val builder1 = AlertDialog.Builder(this)
    builder1.setMessage("Sen derse geldin mi?")
    builder1.setCancelable(true)

    builder1.setPositiveButton("Ne zaman geldim ki") { dialog, id ->
        this.showMessage("Evet")
    }

    builder1.setNegativeButton("Alakam yok") { dialog, id ->
        this.showMessage("hayır")
    }

    val alert11 = builder1.create()
    alert11.show()
}
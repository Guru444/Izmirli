package com.izmir.izmirli.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izmir.izmirli.R
import kotlinx.android.synthetic.main.result_izmir_data_item_layout.view.*

class EczaneNameAdapter : RecyclerView.Adapter<EczaneNameAdapter.IzmirViewHolder>() {

    private var izmirDataList: ArrayList<String?> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IzmirViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.result_izmir_data_item_layout, parent, false)
    )

    override fun onBindViewHolder(holder: EczaneNameAdapter.IzmirViewHolder, position: Int) {
        holder.bind(izmirDataList[position])
    }

    override fun getItemCount(): Int = izmirDataList.size

    inner class IzmirViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(s: String?) {
            itemView.tv_item.text = s?.lowercase()
        }
    }

    fun setEczaneNameList(listIzmir: ArrayList<String?>){
        izmirDataList.clear()
        izmirDataList.addAll(listIzmir)
        notifyDataSetChanged()
    }

    fun clear(){
        izmirDataList.clear()
        notifyDataSetChanged()
    }
}
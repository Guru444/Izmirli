package com.izmir.izmirli.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.izmir.izmirli.R
import com.izmir.izmirli.model.NobetciEczaneResponse
import kotlinx.android.synthetic.main.result_izmir_data_item_layout.view.*

class IzmirAdapter : RecyclerView.Adapter<IzmirAdapter.IzmirViewHolder>() {

    private var izmirDataList: ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem> = arrayListOf()
    var eczaneClickListener : (NobetciEczaneResponse.NobetciEczaneResponseItem) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IzmirViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.result_izmir_data_item_layout, parent, false)
    )

    override fun onBindViewHolder(holder: IzmirAdapter.IzmirViewHolder, position: Int) {
        holder.bind(izmirDataList[position])
    }

    override fun getItemCount(): Int = izmirDataList.size

    inner class IzmirViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(s: NobetciEczaneResponse.NobetciEczaneResponseItem) {
            itemView.tv_item.text = s.adi?.lowercase()?.replaceFirstChar { it.uppercase() }
            s.adi?.let {
                if (it.length > 15){
                    itemView.tv_item.setTextColor(ContextCompat.getColor(itemView.context,
                        R.color.purple_700
                    ))
                }
            }


            itemView.setOnClickListener {
                eczaneClickListener(s)
            }
        }
    }

    fun setIzmirData(listIzmir: ArrayList<NobetciEczaneResponse.NobetciEczaneResponseItem>){
        izmirDataList.clear()
        izmirDataList.addAll(listIzmir)
        notifyDataSetChanged()
    }
    fun clear(){
        izmirDataList.clear()
        notifyDataSetChanged()
    }
}
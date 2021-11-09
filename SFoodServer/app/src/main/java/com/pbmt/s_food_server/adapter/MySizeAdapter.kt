package com.pbmt.s_food_server.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pbmt.s_food_server.R
import com.pbmt.s_food_server.callback.IRecyclerItemClickListener
import com.pbmt.s_food_server.eventbus.SelectSizeModel
import com.pbmt.s_food_server.eventbus.UpdateSizeModel
import com.pbmt.s_food_server.model.SizeModel
import org.greenrobot.eventbus.EventBus

class MySizeAdapter(var context: Context,var sizeModelList: MutableList<SizeModel>):RecyclerView.Adapter<MySizeAdapter.MyViewHolder>() {

    var editPos:Int
    var updateSizeModel:UpdateSizeModel

    init {
        editPos=-1
        updateSizeModel= UpdateSizeModel()
    }
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var txt_name:TextView?=null
        var txt_price:TextView?=null
        var img_delete:ImageView?=null
        internal var listener:IRecyclerItemClickListener?=null

        init {
            txt_name=itemView.findViewById(R.id.txt_name) as TextView
            txt_price=itemView.findViewById(R.id.txt_price) as TextView
            img_delete=itemView.findViewById(R.id.img_delete) as ImageView

            itemView.setOnClickListener { view->
                listener!!.onItemClick(view,adapterPosition)
            }

        }

        fun setListener(listener:IRecyclerItemClickListener?){
            this.listener=listener!!
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_addon_size_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position:Int) {
        holder.txt_name!!.text=sizeModelList[position].name
        holder.txt_price!!.text=sizeModelList[position].price.toString()
        holder.img_delete!!.setOnClickListener {
            sizeModelList.removeAt(position)
            notifyItemRemoved(position)
            updateSizeModel.sizeModelList=sizeModelList
            EventBus.getDefault().postSticky(updateSizeModel)
        }

        holder.setListener(object:IRecyclerItemClickListener{
            override fun onItemClick(view: View, pos: Int) {
                editPos=position
                EventBus.getDefault().postSticky(SelectSizeModel(sizeModelList[pos]))
            }

        })


    }

    override fun getItemCount(): Int {
        return sizeModelList.size
    }

    fun addNewSize(sizeModel: SizeModel) {
        sizeModelList.add(sizeModel)
        notifyItemInserted(sizeModelList.size-1)
        updateSizeModel.sizeModelList=sizeModelList
        EventBus.getDefault().postSticky(updateSizeModel)

    }

    fun editSize(sizeModel: SizeModel) {
        sizeModelList.set(editPos,sizeModel)
        notifyItemChanged(editPos)
        updateSizeModel.sizeModelList=sizeModelList
        EventBus.getDefault().postSticky(updateSizeModel)

    }
}
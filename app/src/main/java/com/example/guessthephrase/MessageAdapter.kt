package com.example.guessthephrase

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class MessageAdapter(val context: Context, val gusees: ArrayList<String>):RecyclerView.Adapter<MessageAdapter.MessageViewHolder> (){
    class MessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val guss = gusees[position]

        holder.itemView.apply {
            tvMessage.text = guss

            if(guss.startsWith("Found")){
                tvMessage.setTextColor(Color.GREEN)
            }else if(guss.startsWith("No")||guss.startsWith("Wrong")){
                tvMessage.setTextColor(Color.RED)
            }else{
                tvMessage.setTextColor(Color.BLACK)
            }
        }
    }


    override fun getItemCount() = gusees.size

}
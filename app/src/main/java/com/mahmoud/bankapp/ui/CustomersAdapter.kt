package com.mahmoud.bankapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.bankapp.R
import com.mahmoud.bankapp.models.User

class CustomersAdapter(val customersList: List<User>) :
    RecyclerView.Adapter<CustomersAdapter.CustomerViewHolder>() {

    private lateinit var listener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.customer_item, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentCustomer = customersList[position]
        holder.customerName.text = "${currentCustomer.firstName} ${currentCustomer.lastName}"
        holder.customerBalance.text = "${currentCustomer.currentBalance} EG"
    }

    override fun getItemCount() = customersList.size

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    inner class CustomerViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val customerName: TextView = viewItem.findViewById(R.id.customer_name)
        val customerBalance: TextView = viewItem.findViewById(R.id.customer_balance)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemClick(itemView, position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int)
    }
}



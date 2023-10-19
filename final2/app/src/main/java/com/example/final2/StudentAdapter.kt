package com.example.final2


import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var stdList: ArrayList<StudentModel> = ArrayList()
    private var onClickItem:((StudentModel) ->Unit)? = null
    private var onClickDeleteItem:((StudentModel) ->Unit)? = null


    fun addItems(items: ArrayList<StudentModel>){
        this.stdList = items
        notifyDataSetChanged()
    }
    fun setOnclickItem(callback: (StudentModel) ->Unit){
        this.onClickItem= callback
    }
    fun setOnClickDeleteItem(callback: (StudentModel) -> Unit){
        this.onClickDeleteItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_item_std, parent, false)
    )

    override fun onBindViewHolder(holder: StudentAdapter.StudentViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(std)}
        holder.btnDelelte.setOnClickListener{onClickDeleteItem?.invoke(std)}
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class StudentViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var email = view.findViewById<TextView>(R.id.tvEmail)
        private var phone = view.findViewById<TextView>(R.id.tvPhone)
        private var pass = view.findViewById<TextView>(R.id.tvPass)
        var btnDelelte = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std: StudentModel) {
            id.text = std.id.toString()
            name.text = std.name
            email.text = std.email
            phone.text = std.phone
            pass.text = std.pass


        }

    }
}


package com.example.final2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    //Declaring Views
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var edPhone: EditText
    private lateinit var edPass: EditText

    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var sqLiteHelper: SQLiteHelper

    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener { addStudent() }
        btnView.setOnClickListener { getStudents() }
        btnUpdate.setOnClickListener { updateStudent() }

        // Set click listeners for RecyclerView items.
        adapter?.setOnclickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()

            //update records or Fill EditText fields with the clicked item's data for updating.
            edName.setText(it.name)
            edEmail.setText(it.email)
            edPhone.setText(it.phone)
            edPass.setText(it.pass)
            std = it
        }
        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)

        }
    }

    private fun getStudents() {
        val stdList = sqLiteHelper.getAllStudent()
        Log.e("listSize", " ${stdList.size}")

        //display-update data in RecyclerView
        adapter?.addItems(stdList)

    }

    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()
        val phone = edPhone.text.toString()
        val pass = edPass.text.toString()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter required fields", Toast.LENGTH_SHORT).show()

        } else {
            val std = StudentModel(name = name, email = email, phone = phone, pass = pass)
            val status = sqLiteHelper.insertStudent(std)
            //check insert success or not
            if (status > -1) {
                Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudents()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()
        val phone = edPhone.text.toString()
        val pass = edPass.text.toString()


        //check record not change
        if (name == std?.name && email == std?.email && phone == std?.phone && pass == std?.pass) {
            Toast.makeText(this, "Record not changed ", Toast.LENGTH_SHORT).show()
            return
        }
        if (std == null) return
        val std = StudentModel(id = std!!.id, name = name, email = email, phone = phone, pass = pass)
        val status = sqLiteHelper.updateStudent(std)
        if (status > -1) {
            clearEditText()
            getStudents()
        } else {
            Toast.makeText(this, "Update Failed ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteStudent(id: Int) {
        if (id == null) return
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item? ")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            sqLiteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText(){
        edName.setText("")
        edEmail.setText("")
        edPhone.setText("")
        edPass.setText("")
        edName.requestFocus()
    }
    private fun initRecyclerView(){

        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView(){
        edName= findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        edPhone = findViewById(R.id.edPhone)
        edPass = findViewById(R.id.edPass)


        btnAdd = findViewById(R.id.btnAdd)
        btnView  = findViewById(R.id.btnView)
        btnUpdate =findViewById(R.id.btnUpdate)
    }
}
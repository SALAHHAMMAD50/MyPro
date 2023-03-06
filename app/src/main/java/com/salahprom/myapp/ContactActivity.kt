package com.salahprom.myapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.salahprom.myapp.databinding.ActivityContactBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding

    private val db = Firebase.firestore
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getContacts()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        binding.buttonSave.setOnClickListener {
            showProgressDialog()
            val name = binding.edName.text.toString()
            val number = binding.edNumber.text.toString()
            val address = binding.edAddress.text.toString()
            val contact = hashMapOf(
                "name" to name,
                "number" to number,
                "address" to address
            )

            // Add a new document with a generated ID

            db.collection("users")
                .add(contact)
                .addOnSuccessListener { documentReference ->
                    hideProfressDialog()
                    contact.clear()
                    binding.edName.setText("")
                    binding.edAddress.setText("")
                    binding.edNumber.setText("")
                    getContacts()
                    Toast.makeText(applicationContext,"DocumentSnapshot added with ID: ${documentReference.id}",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    hideProfressDialog()
                    Toast.makeText(applicationContext,"DocumentSnapshot added with ID: $e",Toast.LENGTH_SHORT).show()
                }
        }


    }
    fun showProgressDialog() {
        progressDialog.show()
    }
    fun hideProfressDialog(){
        progressDialog.dismiss()
    }
    fun getContacts(){
        val listView = binding.listView
        val items = ArrayList<Contact>()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val contact = Contact()
                    contact.id = document.id
                    contact.name= document.getString("name").toString()
                    contact.address= document.getString("address").toString()
                    contact.number= document.getString("number").toString()
                    items.add(contact)
                }
                val adapter = ContactAdapter(this, items, db)
                listView.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext,"DocumentSnapshot added with ID: $exception",Toast.LENGTH_SHORT).show()
            }
    }
}
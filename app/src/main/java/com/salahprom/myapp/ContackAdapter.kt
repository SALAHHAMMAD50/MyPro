package com.salahprom.myapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class ContactAdapter(private val context: Context, private val items: ArrayList<Contact>, private val db: FirebaseFirestore) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items.size.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_cotact, parent, false)
        val nameTextView = view.findViewById<TextView>(R.id.tvName)
        val numberTextView = view.findViewById<TextView>(R.id.tvNumber)
        val addressTextView = view.findViewById<TextView>(R.id.tvAddress)
        val imageView = view.findViewById<ImageView>(R.id.ivDelete)

        imageView.setOnClickListener(){
            db.collection("users").document(items[position].id).delete()
                .addOnSuccessListener {
                    Toast.makeText(context,"success",
                        Toast.LENGTH_SHORT).show()
                    items.removeAt(position)
                    notifyDataSetChanged()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()


                }

        }

        val contact = items[position]
        nameTextView.text = contact.name
        numberTextView.text = contact.number
        addressTextView.text = contact.address

        return view
    }

}
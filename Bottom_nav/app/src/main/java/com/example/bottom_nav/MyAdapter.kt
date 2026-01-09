package com.example.bottom_nav

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.net.HttpURLConnection
import java.net.URL

class MyAdapter(private val arrayList: ArrayList<ModelClass>, private val context: Context) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private val profileImages = listOf(
        R.drawable.wanda_prof,
        R.drawable.cnd_prof,
        R.drawable.jeff_prof,
        R.drawable.sue_prof,
        R.drawable.venom_prof,
        R.drawable.rocket_prof,
        R.drawable.luna_prof,
        R.drawable.ironfist_prof,
        R.drawable.loki_prof,
        R.drawable.emma_prof,
        R.drawable.bruce_prof,
        R.drawable.bw_prof,
        R.drawable.bp_prof,
        R.drawable.adam_prof
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelClass = arrayList[position]

        val randomImage = profileImages.random()
        holder.profileImageView.setImageResource(randomImage)

        holder.username.text = modelClass.username
        holder.uid.text = "UID: "+ modelClass.uid
        holder.caption.text = modelClass.caption




        val thumbnail = modelClass.thumbnail
        Glide.with(holder.itemView.context)
            .load("http://mrivals.x10.mx/upload/$thumbnail")
            .into(holder.imageView)

        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure to delete this post?")
            builder.setTitle("Delete Post")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog, _ ->
                deletePost(modelClass.post_id, position)
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profile_image)
        val username: TextView = itemView.findViewById(R.id.username)
        val uid: TextView = itemView.findViewById(R.id.userid)
        val caption: TextView = itemView.findViewById(R.id.caption)
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_btn)
    }

    fun deletePost(postId: Int, position: Int) {
        Thread {
            try {
                val url = URL("http://mrivals.x10.mx/deletepost.php?post_id=$postId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode
                val response = if (responseCode == 200) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    "Failed to send request. Response code: $responseCode"
                }

                (context as? Activity)?.runOnUiThread {
                    showDialog("Delete Post", response)

                    // Remove the item from the list if deletion was successful
                    if (response.contains("success", ignoreCase = true)) {
                        arrayList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }

            } catch (e: Exception) {
                Log.e("DeletePost", "Error", e)
                (context as? Activity)?.runOnUiThread {
                    showDialog("Delete Post", "An error occurred: ${e.javaClass.simpleName}")
                }
            }
        }.start()
    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
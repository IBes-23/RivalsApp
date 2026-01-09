package com.example.bottom_nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FirstFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private val modelClasses = ArrayList<ModelClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        recyclerView = view.findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchData()

        return view
    }

    private fun fetchData() {
        Thread {
            try {
                val url = URL("http://mrivals.x10.mx/getpost.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    reader.close()
                    inputStream.close()
                    modelClasses.clear()

                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val modelClass = ModelClass(
                            post_id = jsonObject.getInt("post_id"),
                            username = jsonObject.getString("username"),
                            uid = jsonObject.getString("uid"),
                            caption = jsonObject.getString("caption"),
                            thumbnail = jsonObject.getString("thumbnail")
                        )

                        modelClasses.add(modelClass)
                    }

                    modelClasses.sortByDescending { it.post_id }

                    requireActivity().runOnUiThread {
                        myAdapter = MyAdapter(modelClasses, requireContext())
                        recyclerView.adapter = myAdapter
                    }

                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("FetchPosts", "Error: ${e.message}", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}


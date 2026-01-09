package com.example.bottom_nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class FragmentThird : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var uidEditText: EditText
    private lateinit var uploadFileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val profileImageView = view.findViewById<ImageView>(R.id.profile_image_view)

        usernameEditText = view.findViewById(R.id.usernameEditText)
        uidEditText = view.findViewById(R.id.uidEditText)

        val currentUsername = UserSession.username ?: ""
        val currentUID = UserSession.uid ?: ""

        usernameEditText.setText(currentUsername)
        uidEditText.setText(currentUID)

        profileImageView.setOnClickListener {
            showImageSelectionDialog(profileImageView)
        }

        uploadFileButton = view.findViewById(R.id.update_btn)
        uploadFileButton.setOnClickListener {
            sendData(usernameEditText.text.toString(), uidEditText.text.toString())
        }

        return view
    }

    private fun sendData(username: String, uid: String) {
        Thread {
            try {
                val userId = UserSession.userId ?: ""
                val url = URL("http://mrivals.x10.mx/upload.php")
                val connection = url.openConnection() as HttpURLConnection
                val boundary = "*****"
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                connection.doOutput = true
                connection.doInput = true

                val outputStream = DataOutputStream(connection.outputStream)

                fun writeFormField(name: String, value: String) {
                    outputStream.writeBytes("--$boundary\r\n")
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"$name\"\r\n\r\n")
                    outputStream.writeBytes("$value\r\n")
                }

                // Send text fields
                writeFormField("username", username)
                writeFormField("uid", uid)
                writeFormField("user_id", userId)

                outputStream.writeBytes("--$boundary--\r\n")
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

                requireActivity().runOnUiThread {
                    showDialog("Response", if (responseCode == HttpURLConnection.HTTP_OK) responseMessage else "Error $responseCode: $responseMessage")
                }

            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    showDialog("Error", "Failed to send data: ${e.message}")
                }
            }
        }.start()
    }

    private fun showImageSelectionDialog(profileImageView: ImageView) {
        val imageIds = listOf(
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

        val scrollView = ScrollView(requireContext())
        val gridLayout = GridLayout(requireContext()).apply {
            columnCount = 4
            rowCount = (imageIds.size + 3) / 4 // ceiling division
            setPadding(30, 30, 30, 30)
        }

        for (id in imageIds) {
            val img = ImageView(requireContext()).apply {
                setImageResource(id)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 200
                    height = 200
                    setMargins(20, 20, 20, 20)
                }
                setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Set as Profile Picture?")
                        .setPositiveButton("Yes") { _, _ ->
                            profileImageView.setImageResource(id)
                            Toast.makeText(requireContext(), "Profile picture updated!", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            gridLayout.addView(img)
        }

        scrollView.addView(gridLayout)

        AlertDialog.Builder(requireContext())
            .setTitle("Select Profile Picture")
            .setView(scrollView)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}

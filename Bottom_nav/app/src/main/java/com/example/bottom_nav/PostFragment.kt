package com.example.bottom_nav

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PostFragment : Fragment() {

    private lateinit var selectFileButton: Button
    private lateinit var uploadFileButton: Button
    private lateinit var selectedFileTextView: TextView
    private lateinit var txtCaption: TextInputEditText
    private lateinit var txtUsername: TextInputEditText
    private lateinit var txtUID: TextInputEditText
    private var selectedUri: Uri? = null
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        // Initialize views
        selectFileButton = view.findViewById(R.id.select_file_button)
        uploadFileButton = view.findViewById(R.id.upload_file_button)
        selectedFileTextView = view.findViewById(R.id.selected_file_textview)
        txtCaption = view.findViewById(R.id.txtCaption)
        txtUsername = view.findViewById(R.id.username)
        txtUID = view.findViewById(R.id.uid)

        // Set from session
        txtUsername.setText(UserSession.username)
        txtUID.setText(UserSession.uid)

        // File picker setup
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    selectedUri = uri
                    selectedFileTextView.text = getFileName(uri)
                } else {
                    Toast.makeText(requireContext(), "Failed to get file Uri", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "File selection cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        // Select file button
        selectFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            filePickerLauncher.launch(intent)
        }

        // Upload button
        uploadFileButton.setOnClickListener {
            val caption = txtCaption.text?.toString()?.trim() ?: ""
            val username = txtUsername.text?.toString()?.trim() ?: ""
            val uid = txtUID.text?.toString()?.trim() ?: ""

            if (selectedUri != null && caption.isNotEmpty()) {
                uploadFile(selectedUri!!, caption, username, uid)
            } else {
                Toast.makeText(requireContext(), "Fill in all fields and select a file.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun getFileName(uri: Uri): String {
        var fileName: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex("_display_name")
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName ?: "unknown_file"
    }

    private fun uploadFile(uri: Uri, caption: String, username: String, uid: String) {
        Thread {
            try {
                val boundary = "*****"
                val url = URL("http://mrivals.x10.mx/upload_post.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                connection.doOutput = true

                val outputStream = DataOutputStream(connection.outputStream)

                fun writeFormField(name: String, value: String) {
                    outputStream.writeBytes("--$boundary\r\n")
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"$name\"\r\n\r\n")
                    outputStream.writeBytes("$value\r\n")
                }

                // Send form fields
                writeFormField("username", username)
                writeFormField("caption", caption)
                writeFormField("uid", uid)

                // Send file
                outputStream.writeBytes("--$boundary\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${getFileName(uri)}\"\r\n")
                outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n")

                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }

                outputStream.writeBytes("\r\n--$boundary--\r\n")
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                val responseMessage = if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader(InputStreamReader(connection.inputStream)).useLines { it.joinToString("\n") }
                } else {
                    "Upload failed with response code: $responseCode"
                }

                requireActivity().runOnUiThread {
                    showDialog("Upload Status", responseMessage)
                }

            } catch (e: Exception) {
                Log.e("UploadFile", "Upload error", e)
                requireActivity().runOnUiThread {
                    showDialog("Upload Error", e.message ?: "Unknown error")
                }
            }
        }.start()
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

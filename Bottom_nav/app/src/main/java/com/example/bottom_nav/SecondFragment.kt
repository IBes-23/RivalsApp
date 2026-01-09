package com.example.bottom_nav

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)
        val button5 = view.findViewById<Button>(R.id.button5)
        val button6 = view.findViewById<Button>(R.id.button6)
        val button7 = view.findViewById<Button>(R.id.button7)
        val button8 = view.findViewById<Button>(R.id.button8)
        val button9 = view.findViewById<Button>(R.id.button9)
        val button10 = view.findViewById<Button>(R.id.button10)
        val button11 = view.findViewById<Button>(R.id.button11)
        val button12 = view.findViewById<Button>(R.id.button12)
        val button13 = view.findViewById<Button>(R.id.button13)
        val button14 = view.findViewById<Button>(R.id.button14)
        val button15 = view.findViewById<Button>(R.id.button15)
        val button16 = view.findViewById<Button>(R.id.button16)

        // ImageViews for different popups
        val lunaImage = view.findViewById<ImageView>(R.id.luna)
        val cndImage = view.findViewById<ImageView>(R.id.cnd)
        val lokiImage = view.findViewById<ImageView>(R.id.loki)
        val wandaImage = view.findViewById<ImageView>(R.id.wanda)
        val stormImage = view.findViewById<ImageView>(R.id.storm)
        val spiderImage = view.findViewById<ImageView>(R.id.spidey)
        val venomImage = view.findViewById<ImageView>(R.id.venom)
        val grootImage = view.findViewById<ImageView>(R.id.groot)
        val strangeImage = view.findViewById<ImageView>(R.id.drstrange)

        lunaImage.setOnClickListener {
            showImagePopup(R.drawable.lunapopup)
        }
        cndImage.setOnClickListener {
            showImagePopup(R.drawable.cndpopup)
        }
        lokiImage.setOnClickListener {
            showImagePopup(R.drawable.lokipopup)
        }
        wandaImage.setOnClickListener {
            showImagePopup(R.drawable.wandapopup)
        }
        stormImage.setOnClickListener {
            showImagePopup(R.drawable.stormpopup)
        }
        spiderImage.setOnClickListener {
            showImagePopup(R.drawable.spiderpopup)
        }
        venomImage.setOnClickListener {
            showImagePopup(R.drawable.venompopup)
        }
        grootImage.setOnClickListener {
            showImagePopup(R.drawable.grootpopup)
        }
        strangeImage.setOnClickListener {
            showImagePopup(R.drawable.drstrangepopup)
        }

        button1.setOnClickListener {
            val url = "https://youtu.be/dg25UGYWsbg?si=t1GlUj1DQOrmWEnr"
            showExitDialog(url)
        }
        button2.setOnClickListener {
            val url = "https://youtu.be/kZWpBlJyqGU?si=WHeZPqj1CVF7_yJl"
            showExitDialog(url)
        }
        button3.setOnClickListener {
            val url = "https://youtu.be/M2FK--iWF5c?si=3VrmmvgKbdKFxX-1"
            showExitDialog(url)
        }
        button7.setOnClickListener {
            val url = "https://youtu.be/rG_tXbvM3u8?si=fYa95HXlYFgIBC18"
            showExitDialog(url)
        }
        button8.setOnClickListener {
            val url = "https://youtu.be/lfVw8koF5YQ?si=jjD3DO6A0XTheKtD"
            showExitDialog(url)
        }
        button9.setOnClickListener {
            val url = "https://youtu.be/Mwf3_k7gffw?si=OtQEeK8am6LDqdSj"
            showExitDialog(url)
        }
        button13.setOnClickListener {
            val url = "https://youtu.be/jzICK22y7Is?si=6SlI4k-ei2FYFASZ"
            showExitDialog(url)
        }
        button14.setOnClickListener {
            val url = "https://youtu.be/8bwy9xgLlk4?si=VkDoVYTiwzbQ6zg6"
            showExitDialog(url)
        }
        button15.setOnClickListener {
            val url = "https://youtu.be/zwM6YQ4v_SI?si=FkPm35lwke20s4Rm"
            showExitDialog(url)
        }


        return view
    }
    private fun showExitDialog(url: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Do you want to exit the app and open YouTube?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Open YouTube when "Yes" is pressed
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                activity?.finish() // Close the app if YouTube opens
            }
            .setNegativeButton("No") { dialog, id ->
                // Do nothing when "No" is pressed
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }


    // Reusable function for showing image popups
    private fun showImagePopup(imageResId: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_image_popup)

        val LunaimageView: ImageView = dialog.findViewById(R.id.lunapopup)
        LunaimageView.setImageResource(imageResId)

        val CndimageView: ImageView = dialog.findViewById(R.id.cndpopup)
        CndimageView.setImageResource(imageResId)

        val LokiimageView: ImageView = dialog.findViewById(R.id.lokipopup)
        LokiimageView.setImageResource(imageResId)

        val WandaimageView: ImageView = dialog.findViewById(R.id.wandapopup)
        WandaimageView.setImageResource(imageResId)

        val StormimageView: ImageView = dialog.findViewById(R.id.stormpopup)
        StormimageView.setImageResource(imageResId)

        val SpiderimageView: ImageView = dialog.findViewById(R.id.spiderpopup)
        SpiderimageView.setImageResource(imageResId)

        val VenomimageView: ImageView = dialog.findViewById(R.id.venompopup)
        VenomimageView.setImageResource(imageResId)

        val GrootimageView: ImageView = dialog.findViewById(R.id.grootpopup)
        GrootimageView.setImageResource(imageResId)

        val DrStrangeimageView: ImageView = dialog.findViewById(R.id.drstrangepopup)
        DrStrangeimageView.setImageResource(imageResId)


        dialog.show()
    }
}

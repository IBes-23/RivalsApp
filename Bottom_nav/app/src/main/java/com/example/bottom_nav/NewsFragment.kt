package com.example.bottom_nav

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.VideoView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.constants.ScaleTypes

class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        val videoView = view.findViewById<VideoView>(R.id.ultronvid)
        val uri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.ultronvid}")
        videoView.setVideoURI(uri)

        val mediaController = android.widget.MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setOnPreparedListener {
            videoView.start()
        }

        videoView.setOnCompletionListener {
            videoView.start()
        }

        val imageSlider = view.findViewById<ImageSlider>(R.id.imageslider)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.patchnote3))
        imageList.add(SlideModel(R.drawable.patchnote1))
        imageList.add(SlideModel(R.drawable.update1))
        imageList.add(SlideModel(R.drawable.update2))
        imageList.add(SlideModel(R.drawable.update3))
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        // Second Image Slider
        val imageSlider2 = view.findViewById<ImageSlider>(R.id.imageslider2)
        val imageList2 = ArrayList<SlideModel>()
        imageList2.add(SlideModel(R.drawable.map1))
        imageList2.add(SlideModel(R.drawable.map2))
        imageList2.add(SlideModel(R.drawable.map3))
        imageSlider2.setImageList(imageList2, ScaleTypes.FIT)

        // Button to navigate to SecondFragment
        val button = view.findViewById<Button>(R.id.btnheroes)
        button.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, SecondFragment())
                .commit()
        }

        return view
    }
}
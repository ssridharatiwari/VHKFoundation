package com.vhkfoundation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import com.smarteist.autoimageslider.SliderView
import com.vhkfoundation.R
import com.vhkfoundation.adapter.SliderAdapter2
import com.vhkfoundation.model.SliderData

import com.vhkfoundation.activity.BaseActivity

class SlideImageActivity : BaseActivity() {

    private lateinit var sliderView: SliderView
    private val sliderDataArrayList = listOf(
        SliderData(R.drawable.slide_img1,R.drawable.slide_img2),
        SliderData(R.drawable.slide_img1,R.drawable.slide_img2),
        SliderData(R.drawable.slide_img1,R.drawable.slide_img2),
        // Add more images as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_image)

        sliderView = findViewById(R.id.slider)
        val adapter = SliderAdapter2(this, sliderDataArrayList)
        sliderView.apply {
            autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            setSliderAdapter(adapter)
            scrollTimeInSec = 3
            isAutoCycle = true
            startAutoCycle()
        }

        // Handler to monitor when the last image is shown
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val currentPosition = sliderView.currentPagePosition
                if (currentPosition == sliderDataArrayList.size - 1) {
                    // Navigate to the next activity when the last image is reached
                    val intent = Intent(this@SlideImageActivity, ActivityLogin::class.java)
                    startActivity(intent)
                    finish() // Optional: to finish the current activity and prevent back navigation
                } else {
                    handler.postDelayed(this, 3000) // Continue checking every 3 seconds
                }
            }
        }

        // Start checking after the auto-cycle begins
        handler.postDelayed(runnable, 3000)
    }
}

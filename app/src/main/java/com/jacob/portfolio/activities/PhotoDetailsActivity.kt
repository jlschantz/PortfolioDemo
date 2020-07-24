package com.jacob.portfolio.activities

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.jacob.portfolio.R
import kotlinx.android.synthetic.main.activity_photo_details.*

class PhotoDetailsActivity : AppCompatActivity() {

    private var TAG : String = "PhotoDetailActivity"
    private var photoDetailUrl : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        if(savedInstanceState!=null) {
            photoDetailUrl = savedInstanceState.getString("photoDetailUrl")
        }else{
            photoDetailUrl = intent.getStringExtra("photoDetailUrl")
        }

        setSupportActionBar(activity_photo_detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        initImageView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.v(TAG,"onOptionsItemSelected")
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("photoDetailUrl", photoDetailUrl)
    }

    private fun initImageView() {
        val url = GlideUrl(
            photoDetailUrl, LazyHeaders.Builder()
                .addHeader("User-Agent", "user-agent")
                .build()
        )
        Glide.with(this)
            .load(url)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                    activity_photo_detail_tv_empty.visibility = View.VISIBLE
                    activity_photo_detail_pb_progress.visibility = View.GONE
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    activity_photo_detail_iv.visibility = View.VISIBLE
                    activity_photo_detail_pb_progress.visibility = View.GONE
                    return false
                }
            })
            .into(activity_photo_detail_iv)
    }
}
package com.jacob.portfolio.activities

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jacob.portfolio.R
import com.jacob.portfolio.adapters.PhotosAdapter
import com.jacob.portfolio.models.PhotoItem
import com.jacob.portfolio.utilities.CommonUtilities
import com.jacob.portfolio.viewmodels.PhotosViewModel
import com.jacob.portfolio.viewmodels.SharedViewModelFactory
import kotlinx.android.synthetic.main.activity_photos.*


class PhotosActivity : AppCompatActivity(), PhotosAdapter.OnItemClickListener {
    private var TAG : String = "PhotosActivity"
    private var photosAdapter: PhotosAdapter? = null
    private lateinit var photosViewModel : PhotosViewModel
    private var photosLocal: List<PhotoItem> = ArrayList<PhotoItem>()
    private val commonUtilities : CommonUtilities = CommonUtilities()
    private var albumId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        if(savedInstanceState!=null) {
            albumId = savedInstanceState.getInt("albumId")
        }else{
            albumId = intent.getIntExtra("albumId", 0)
        }

        setSupportActionBar(activity_photos_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        initRecyclerView()
        setupPhotosViewModel()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("albumId", albumId)
    }

    private fun initRecyclerView() {
        //Log.v(TAG,"initRecyclerView")
        val numberOfColumns: Int = commonUtilities.calculateNoOfColumns(this,100)
        val layoutManager : androidx.recyclerview.widget.RecyclerView.LayoutManager =
            androidx.recyclerview.widget.GridLayoutManager(this, numberOfColumns)
        activity_photos_rv_list.layoutManager = layoutManager

        photosAdapter = PhotosAdapter(
            photosLocal,
            this,
            this
        )
        activity_photos_rv_list.adapter = photosAdapter
    }

    private fun setupPhotosViewModel(){
        //photosViewModel = ViewModelProvider(activity!!).get<PhotosViewModel>(PhotosViewModel::class.java)
        photosViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(application = applicationContext as Application,albumId = albumId)
        ).get(
            PhotosViewModel::class.java
        )

        photosViewModel.getPhotos()!!
            .observe(this, androidx.lifecycle.Observer {
                Log.v(TAG,"photos updated")
                Log.v(TAG,"photos updated $it")
                photosLocal = it
            })
        photosViewModel.getProgress()!!
            .observe(this, androidx.lifecycle.Observer {
                Log.v(TAG,"photos displayed")
                if (it == true) {
                    turnOnProgressBar()
                } else {
                    setupUsersDisplay()
                    turnOffProgressBar()
                    handleErrorDisplay()
                }
            })
    }

    override fun onItemClick(photoItem: PhotoItem) {

    }

    private fun turnOnProgressBar(){
        Log.v(TAG,"turnOnProgressBar")
        activity_photos_rv_list.visibility = View.GONE
        activity_photos_pb_progress.visibility = View.VISIBLE
    }

    private fun turnOffProgressBar(){
        Log.v(TAG,"turnOffProgressBar")
        activity_photos_rv_list.visibility = View.VISIBLE
        activity_photos_pb_progress.visibility = View.GONE
    }

    private fun setupUsersDisplay() {
        //Log.v(TAG, "setupOpeningsDisplay")

        photosAdapter?.setItems(photosLocal)
        turnOffProgressBar()
    }

    private fun handleErrorDisplay(){
        if (photosLocal.isEmpty()) {
            activity_photos_rv_list.visibility= View.GONE
            activity_photos_tv_empty.visibility= View.VISIBLE
            handleError()
        }else{
            activity_photos_tv_empty.visibility= View.GONE
            activity_photos_rv_list.visibility= View.VISIBLE
        }
    }

    private fun handleError() {
        Log.v(TAG, "handleError")

        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.error_text))
        builder.setNeutralButton(getString(R.string.error_text_ok)){ dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
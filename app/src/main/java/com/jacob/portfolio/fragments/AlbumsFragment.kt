package com.jacob.portfolio.fragments

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jacob.portfolio.R
import com.jacob.portfolio.activities.MainInterface
import com.jacob.portfolio.adapters.AlbumsAdapter
import com.jacob.portfolio.models.AlbumItem
import com.jacob.portfolio.utilities.CommonUtilities
import com.jacob.portfolio.viewmodels.AlbumsViewModel
import com.jacob.portfolio.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_albums.*

class AlbumsFragment : Fragment(), AlbumsAdapter.OnItemClickListener{

    private var TAG : String = "AlbumFragment"
    private lateinit var mainInterface : MainInterface
    private var albumsAdapter: AlbumsAdapter? = null
    private lateinit var albumsViewModel : AlbumsViewModel
    private var albumsLocal: List<AlbumItem> = ArrayList<AlbumItem>()
    private val commonUtilities : CommonUtilities = CommonUtilities()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v(TAG,"onAttach")
        mainInterface = activity as MainInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.v(TAG,"onCreateView")
        val view : View = inflater.inflate(R.layout.fragment_albums,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG,"onViewCreated")
        initRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG,"onActivityCreated")

        setupAlbumsViewModel()
    }

    private fun initRecyclerView() {
        val layoutManager : androidx.recyclerview.widget.RecyclerView.LayoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context)
        fragment_albums_rv_list.layoutManager = layoutManager

        albumsAdapter = AlbumsAdapter(
            albumsLocal,
            this,
            context!!
        )
        fragment_albums_rv_list.adapter = albumsAdapter
    }

    private fun setupAlbumsViewModel(){
        //albumsViewModel = ViewModelProvider(activity!!).get<AlbumsViewModel>(AlbumsViewModel::class.java)
        albumsViewModel = ViewModelProvider(
            activity!!,
            ViewModelProvider.AndroidViewModelFactory.getInstance(context?.applicationContext as Application)
        ).get(
            AlbumsViewModel::class.java
        )

        albumsViewModel.getAlbums()!!
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                albumsLocal = it
            })
        albumsViewModel.getProgress()!!
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it == true) {
                    turnOnProgressBar()
                } else {
                    setupUsersDisplay()
                    turnOffProgressBar()
                    handleErrorDisplay()
                }
            })
    }

    override fun onItemClick(albumItem: AlbumItem) {
        mainInterface.goToPhotosForAlbum(albumItem)
    }

    private fun turnOnProgressBar(){
        fragment_albums_rv_list.visibility = View.GONE
        fragment_albums_pb_progress.visibility = View.VISIBLE
    }

    private fun turnOffProgressBar(){
        fragment_albums_rv_list.visibility = View.VISIBLE
        fragment_albums_pb_progress.visibility = View.GONE
    }

    private fun setupUsersDisplay() {
        albumsAdapter?.setItems(albumsLocal)
        turnOffProgressBar()
    }

    private fun handleErrorDisplay(){
        if (albumsLocal.isEmpty()) {
            fragment_albums_rv_list.visibility=View.GONE
            fragment_albums_tv_empty.visibility=View.VISIBLE
            handleError()
        }else{
            fragment_albums_tv_empty.visibility=View.GONE
            fragment_albums_rv_list.visibility=View.VISIBLE
        }
    }

    private fun handleError() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(getString(R.string.error_text))
        builder.setNeutralButton(getString(R.string.error_text_ok)){ dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
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
import com.jacob.portfolio.adapters.UsersAdapter
import com.jacob.portfolio.models.UserItem
import com.jacob.portfolio.utilities.CommonUtilities
import com.jacob.portfolio.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment(), UsersAdapter.OnItemClickListener {

    private var TAG : String = "UsersFragment"
    private lateinit var mainInterface : MainInterface
    private var usersAdapter: UsersAdapter? = null
    private lateinit var usersViewModel : UsersViewModel
    private var usersLocal: List<UserItem> = ArrayList<UserItem>()
    private val commonUtilities : CommonUtilities = CommonUtilities()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v(TAG,"onAttach")
        mainInterface = activity as MainInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.v(TAG,"onCreateView")
        val view : View = inflater.inflate(R.layout.fragment_users,container,false)
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

        setupOpeningsViewModel()
    }

    private fun initRecyclerView() {
        //Log.v(TAG,"initRecyclerView")
        val layoutManager : androidx.recyclerview.widget.RecyclerView.LayoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context)
        fragment_users_rv_list.layoutManager = layoutManager

        usersAdapter = UsersAdapter(
            usersLocal,
            this,
            context!!
        )
        fragment_users_rv_list.adapter = usersAdapter
    }

    private fun setupOpeningsViewModel(){
        //usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        usersViewModel = ViewModelProvider(
            activity!!,
            ViewModelProvider.AndroidViewModelFactory.getInstance(context?.applicationContext as Application)
        ).get(
            UsersViewModel::class.java
        )
        usersViewModel.getUsers()!!
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Log.v(TAG,"users updated")
                Log.v(TAG,"users updated $it")
                usersLocal = it
            })
        usersViewModel.getProgress()!!
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Log.v(TAG,"users displayed")
                if (it == true) {
                    turnOnProgressBar()
                } else {
                    setupUsersDisplay()
                    turnOffProgressBar()
                    handleErrorDisplay()
                }
            })
    }

    override fun onItemClick(userItem: UserItem) {
        mainInterface.goToPhotosForUser(userItem)
    }

    private fun turnOnProgressBar(){
        Log.v(TAG,"turnOnProgressBar")
        fragment_users_rv_list.visibility = View.GONE
        fragment_users_pb_progress.visibility = View.VISIBLE
    }

    private fun turnOffProgressBar(){
        Log.v(TAG,"turnOffProgressBar")
        fragment_users_rv_list.visibility = View.VISIBLE
        fragment_users_pb_progress.visibility = View.GONE
    }

    private fun setupUsersDisplay() {
        //Log.v(TAG, "setupOpeningsDisplay")
        usersAdapter?.setItems(usersLocal)

        turnOffProgressBar()
    }

    private fun handleErrorDisplay(){
        if (usersLocal.isEmpty()) {
            fragment_users_rv_list.visibility=View.GONE
            fragment_users_tv_empty.visibility=View.VISIBLE
            handleError()
        }else{
            fragment_users_tv_empty.visibility=View.GONE
            fragment_users_rv_list.visibility=View.VISIBLE
        }
    }

    private fun handleError() {
        Log.v(TAG, "handleError")

        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(getString(R.string.error_text))
        builder.setNeutralButton(getString(R.string.error_text_ok)){ dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
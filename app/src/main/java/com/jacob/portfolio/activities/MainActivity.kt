package com.jacob.portfolio.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.jacob.portfolio.R
import com.jacob.portfolio.fragments.AlbumsFragment
import com.jacob.portfolio.fragments.UsersFragment
import com.jacob.portfolio.models.AlbumItem
import com.jacob.portfolio.models.UserItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    MainInterface {

    private var TAG : String = "MainActivity"

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private var currentFragment : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationDrawer()
        setupFragment(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.v(TAG,"onPostCreate")
        actionBarDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.v(TAG,"onConfigurationChanged")
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.v(TAG,"onOptionsItemSelected")
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.v(TAG,"onNavigationItemSelected")

        when(item.itemId){
            R.id.nav_item_list -> {
                val fragment = UsersFragment()
                doFragmentTransaction(fragment,getString(R.string.fragment_users))
            }
            R.id.nav_item_album -> {
                val fragment = AlbumsFragment()
                doFragmentTransaction(fragment,getString(R.string.fragment_albums))
            }
            else -> {

            }
        }
        activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        Log.v(TAG,"onBackPressed")
        if (activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.v(TAG,"onSaveInstanceState")
        outState.putString("currentFragment", currentFragment)
    }

    private fun setupNavigationDrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this,
            activity_main_drawer_layout, activity_main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        activity_main_drawer_layout.addDrawerListener(actionBarDrawerToggle)
        activity_main_nav_view.setNavigationItemSelectedListener(this)
        setSupportActionBar(activity_main_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun doFragmentTransaction(fragment: Fragment, tag: String){
        Log.v(TAG,"doFragmentTransaction")
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_main_fl_fragment_container,fragment,tag)

        currentFragment = tag
        transaction.commit()
    }

    private fun setupFragment(savedInstanceState: Bundle?) {
        Log.v(TAG,"setupSourceAndFragment")
        if(savedInstanceState == null){
            currentFragment = getString(R.string.fragment_users)
            val fragment = UsersFragment()
            doFragmentTransaction(fragment,getString(R.string.fragment_users))
            activity_main_nav_view.setCheckedItem(R.id.nav_item_list)
        }
    }

    override fun goToPhotosForAlbum(albumItem: AlbumItem) {
        val intent = Intent(this, PhotosActivity::class.java)
        intent.putExtra("albumId", albumItem.id)
        startActivity(intent)
    }

    override fun goToPhotosForUser(userItem: UserItem) {

    }
}

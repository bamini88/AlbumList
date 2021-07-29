package com.coding.albums.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding.albums.R
import com.coding.albums.adapter.RecyclerAdapter
import com.coding.albums.misc.VerticalSpacingItemDecorator
import com.coding.albums.model.Album
import com.coding.albums.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerAdapter? = null
    private val serviceGenerator: ServiceGenerator? = ServiceGenerator.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)

        initRecyclerView()
        listApi()
    }

    private fun listApi() {
        serviceGenerator?.api?.getAlbums("albums")
            ?.enqueue(object : Callback<List<Album?>?> {
                override fun onResponse(
                    call: Call<List<Album?>?>,
                    response: Response<List<Album?>?>
                ) {
                    Log.d(TAG, "onResponse: " + response.body())
                    if (response.raw().networkResponse() != null) {
                        Log.d(TAG, "onResponse: response is from NETWORK...")
                    } else if (response.raw().cacheResponse() != null
                        && response.raw().networkResponse() == null
                    ) {
                        Log.d(TAG, "onResponse: response is from CACHE...")
                    }
                    if (response.body() == null) {
                        adapter!!.setAlbums(ArrayList())
                    } else {

                        adapter!!.setAlbums(
                            (response.body()!!.sortedBy { it?.title } as List<Album>))
                    }
                }

                override fun onFailure(call: Call<List<Album?>?>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    adapter!!.setAlbums(ArrayList())
                }
            })
    }

    private fun initRecyclerView() {
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val itemDecorator = VerticalSpacingItemDecorator(20)

        recyclerView!!.addItemDecoration(itemDecorator)
        adapter = RecyclerAdapter()
        recyclerView!!.adapter = adapter
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}
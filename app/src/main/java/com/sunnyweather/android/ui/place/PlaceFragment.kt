package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {
    //懒加载，使viewModel可以随时使用，不用关心它何时初始化、是否为空
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java)}
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //判断如果存储有城市的数据同时如果PlaceFragment被嵌入MainActivity中，
        //就获取数据并解析成Place对象，然后传递给WeatherActivity
        if(activity is MainActivity && viewModel.isPlaceSaved()){
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }


        val layoutManager = LinearLayoutManager(activity)
        //findViewById是View类的方法，应该在View对象的上下文中使用，不能直接在fragment中使用
        val recyclerView : RecyclerView? = view?.findViewById(R.id.recyclerView)
        val searchPlaceEdit : EditText? = view?.findViewById(R.id.searchPlaceEdit)
        val bgImageView : ImageView? = view?.findViewById(R.id.bgImageView)

        recyclerView?.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        recyclerView?.adapter = adapter

        searchPlaceEdit?.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                //如果搜索框不为空则搜索该城市
                Log.d("PlaceFragment", "搜索框不为空，搜索该城市,livedata = ${viewModel.placeLiveData}")
                viewModel.searchPlaces(content)
            }else{
                //若搜索框为空则显示背景图片，隐藏recyclerview
                recyclerView?.visibility = View.GONE
                bgImageView?.visibility = View.VISIBLE
                viewModel.placeList.clear()
                Log.d("PlaceFragment", "搜索框为空,livedata = ${viewModel.placeLiveData}")
                adapter.notifyDataSetChanged()
            }
        //liveData用来接受搜索返回的数据，通过监听liveData的改变来通知adapter刷新界面显示
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null){
                Log.d("PlaceFragment", "搜索结果不为空,result = $result,places = $places")
                recyclerView?.visibility = View.VISIBLE
                bgImageView?.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                Log.d("PlaceFragment", "viewModel.placeList = ${viewModel.placeList}")
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity, "未能查询到任何地点" ,Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        }
    }
}
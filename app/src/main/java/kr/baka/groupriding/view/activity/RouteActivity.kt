package kr.baka.groupriding.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_route.*
import kr.baka.groupriding.R
import kr.baka.groupriding.adapter.RouteAdapter
import kr.baka.groupriding.databinding.ActivityRouteBinding
import kr.baka.groupriding.repository.RouteRepository
import kr.baka.groupriding.viewmodel.RouteViewModel

class RouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.colorPrimary)
        val binding = DataBindingUtil.setContentView<ActivityRouteBinding>(this, R.layout.activity_route)
        val viewModel = RouteViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = RouteAdapter({ route ->}, { route -> })

        val lm = LinearLayoutManager(this)
        routeRecyclerView.adapter = adapter
        routeRecyclerView.layoutManager = lm
        routeRecyclerView.setHasFixedSize(true)

        val list = RouteRepository(this).allRoute

        list.observe(this, Observer {
            Log.e("dd",it.size.toString())
            for (i in it.indices){
                Log.e( it[i].rid.toString(), it[i].name)
            }

            adapter.setContacts(it)
        })



    }
}

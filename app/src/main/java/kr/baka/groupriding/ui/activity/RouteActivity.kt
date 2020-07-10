package kr.baka.groupriding.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_route.*
import kr.baka.groupriding.R
import kr.baka.groupriding.adapter.RouteAdapter
import kr.baka.groupriding.databinding.ActivityRouteBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.ViewModelFactory
import kr.baka.groupriding.repository.RouteRepository
import kr.baka.groupriding.ui.dialog.RecordRouteSetDialog
import kr.baka.groupriding.viewmodel.MainViewModel
import kr.baka.groupriding.viewmodel.RouteViewModel

class RouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.colorPrimary)
        val binding = DataBindingUtil.setContentView<ActivityRouteBinding>(this, R.layout.activity_route)
        val viewModel = RouteViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val mainViewModel = ViewModelProvider(App(), ViewModelFactory()).get(MainViewModel::class.java)

        val adapter = RouteAdapter({ route ->
            RecordRouteSetDialog.getInstance(route).also { dialog->
                dialog.setLeftButton(View.OnClickListener {
                    dialog.dismiss()
                })
                dialog.setRightButton(View.OnClickListener {
                    mainViewModel.route.value = dialog.arrayListLatLng
                    dialog.dismiss()
                    finish()
                })
                dialog.show(supportFragmentManager,"dialog")
            }
        }, { route ->
                RouteRepository(this).delete(route)
        })

        val lm = LinearLayoutManager(this)
        routeRecyclerView.adapter = adapter
        routeRecyclerView.layoutManager = lm
        routeRecyclerView.setHasFixedSize(true)

        RouteRepository(this).getAllRoute().observe(this, Observer {
            for (i in it.indices){
                Log.e( it[i].rid.toString(), it[i].name)
            }
            adapter.setContacts(it)
        })



    }
}

package kr.baka.groupriding.viewmodel

import android.view.View
import androidx.lifecycle.*
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.etc.ViewModelFactory
import kr.baka.groupriding.repository.ServiceStatusLiveData

class MenuViewModel :ViewModel(){

    val mainViewModel = ViewModelProvider(App(), ViewModelFactory()).get(MainViewModel::class.java)

    val startGroupRidingServiceEvent = SingleLiveData<Any>()
    fun startGroupRidingService(){
        startGroupRidingServiceEvent.call()
    }

    val stopGroupRidingServiceEvent = SingleLiveData<Any>()
    fun stopGroupRidingService(){
        stopGroupRidingServiceEvent.call()
    }

    val showSettingActivityEvent = SingleLiveData<Any>()
    fun showSettingActivity(){
        showSettingActivityEvent.call()
    }

    val inviteCodeDialogShowEvent = SingleLiveData<Any>()
    fun inviteCodeDialogShow(){
        inviteCodeDialogShowEvent.call()
    }

    val showRouteActivityEvent = SingleLiveData<Any>()
    fun showRouteActivity(){
        showRouteActivityEvent.call()
    }

    val showRecordRouteDialogEvent = SingleLiveData<Any>()
    fun showRecordRouteDialog(){
        showRecordRouteDialogEvent.call()
    }

    val showRecordRouteStopDialogEvent = SingleLiveData<Any>()
    fun showRecordRouteStopDialog(){
        showRecordRouteStopDialogEvent.call()
    }

    val showRouteStopDialogEvent = SingleLiveData<Any>()
    fun showRouteStopDialog(){
        showRouteStopDialogEvent.call()
    }

    val routeStartVisibility = MediatorLiveData<Int>().also { liveData->
        liveData.addSource(mainViewModel.route, Observer {
            if (it!=null) liveData.value = View.VISIBLE
            else liveData.value = View.GONE
        })
    }

    val routeStopVisibility = MediatorLiveData<Int>().also { liveData->
        liveData.addSource(mainViewModel.route, Observer {
            if (it==null) liveData.value = View.VISIBLE
            else liveData.value = View.GONE
        })
    }

    val groupRidingStartVisibility = MediatorLiveData<Int>().also { liveData->
        liveData.addSource(ServiceStatusLiveData.groupingService, Observer {
            if (it) liveData.value = View.VISIBLE
            else liveData.value = View.GONE
        })
    }

    val groupRidingStopVisibility = MediatorLiveData<Int>().also { liveData ->
        liveData.addSource(ServiceStatusLiveData.groupingService, Observer {
            if (!it) liveData.value = View.VISIBLE
            else liveData.value = View.GONE
        })
    }

    val recordRouteStartVisibility = MediatorLiveData<Int>().also { liveData ->
        liveData.addSource(ServiceStatusLiveData.recordingService, Observer {
            if (it) liveData.value = View.VISIBLE
            else liveData.value = View.GONE
        })
    }
    val recordRouteStopVisibility = MediatorLiveData<Int>().also { liveData ->
        liveData.addSource(ServiceStatusLiveData.recordingService, Observer {
            if (!it) liveData.value = View.VISIBLE
            else liveData.value = View.GONE
        })
    }

}
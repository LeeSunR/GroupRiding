package kr.baka.groupriding.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kr.baka.groupriding.databinding.FragmentMenuBinding
import kr.baka.groupriding.repository.ServiceStatusLiveData
import kr.baka.groupriding.ui.activity.RouteActivity
import kr.baka.groupriding.ui.activity.SettingActivity
import kr.baka.groupriding.ui.dialog.*
import kr.baka.groupriding.viewmodel.MenuViewModel

class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        val viewModel = MenuViewModel()
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.startGroupRidingServiceEvent.observe(this, Observer {
            if  (ServiceStatusLiveData.recordingService.value==false)
                GroupRidingStartDialog(context!!).show()
            else
                FailDialog(context!!,"오류","녹화중에는 그룹라이딩 참가/생성이 불가능합니다").show()
        })

        viewModel.stopGroupRidingServiceEvent.observe(this, Observer {
            GroupRidingStopDialog(context!!).show()
        })

        viewModel.inviteCodeDialogShowEvent.observe(this, Observer {
            GroupRidingInviteDialog(context!!).show()
        })

        viewModel.showRecordRouteDialogEvent.observe(this, Observer {
            if (ServiceStatusLiveData.groupingService.value==false)
                RecordRouteDialog(context!!).show()
            else
                FailDialog(context!!,"오류","그룹라이딩 실행중 경로 녹화가 불가능합니다").show()
        })

        viewModel.showRecordRouteStopDialogEvent.observe(this, Observer {
            RecordRouteStopDialog(context!!).show()
        })

        viewModel.showRouteActivityEvent.observe(this, Observer {
            val intent = Intent(context, RouteActivity::class.java)
            startActivity(intent)
        })

        viewModel.showSettingActivityEvent.observe(this, Observer {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        })

        return binding.root
    }

    companion object{
        fun newInstance():MenuFragment {
            return MenuFragment()
        }
    }
}

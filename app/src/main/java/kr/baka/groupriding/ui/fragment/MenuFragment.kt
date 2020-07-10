package kr.baka.groupriding.ui.fragment

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.FragmentMenuBinding
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.ViewModelFactory
import kr.baka.groupriding.repository.ServiceStatusLiveData
import kr.baka.groupriding.repository.SettingRepository
import kr.baka.groupriding.service.GroupRidingService
import kr.baka.groupriding.service.RecordRouteService
import kr.baka.groupriding.ui.activity.RouteActivity
import kr.baka.groupriding.ui.activity.SettingActivity
import kr.baka.groupriding.ui.dialog.*
import kr.baka.groupriding.viewmodel.MainViewModel
import kr.baka.groupriding.viewmodel.MenuViewModel

class MenuFragment : Fragment() {

    companion object{
        fun newInstance():MenuFragment {
            return MenuFragment()
        }
    }

    private val groupCreateCompletedBroadcastReceiver = GroupCreateCompletedBroadcastReceiver()
    private val groupCreateCompletedFilter = IntentFilter().also { it.addAction("GroupCreateCompletedBroadcast") }
    private val mainViewModel = ViewModelProvider(App(), ViewModelFactory()).get(MainViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        val viewModel = MenuViewModel()
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.startGroupRidingServiceEvent.observe(this, Observer {
            if (ServiceStatusLiveData.recordingService.value==false) createGroupRidingStartDialog()
            else FailDialog(context!!,"오류","녹화중에는 그룹라이딩 참가/생성이 불가능합니다").show()
        })

        viewModel.stopGroupRidingServiceEvent.observe(this, Observer {
            createGroupRidingStopDialog()
        })

        viewModel.inviteCodeDialogShowEvent.observe(this, Observer {
            createGroupRidingInviteDialog()
        })

        viewModel.showRecordRouteDialogEvent.observe(this, Observer {
            if (ServiceStatusLiveData.groupingService.value==false) createRecordRouteStartDialog()
            else FailDialog(context!!,"오류","그룹라이딩 실행중 경로 녹화가 불가능합니다").show()
        })

        viewModel.showRecordRouteStopDialogEvent.observe(this, Observer {
            createRecordRouteStopDialog()
        })

        viewModel.showRouteActivityEvent.observe(this, Observer {
            val intent = Intent(context, RouteActivity::class.java)
            startActivity(intent)
        })

        viewModel.showSettingActivityEvent.observe(this, Observer {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        })

        viewModel.showRouteStopDialogEvent.observe(this, Observer {
            createRouteShowStopDialog()
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context!!.registerReceiver(groupCreateCompletedBroadcastReceiver, groupCreateCompletedFilter)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        context!!.unregisterReceiver(groupCreateCompletedBroadcastReceiver)
        super.onDestroy()
    }

    private fun createRouteShowStopDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_routeShowStop))
            dialog.setMessage(getString(R.string.dialogMessage_routeShowStop))
            dialog.setButtonStyle(SimpleDialog.STYLE_DANGER)
            dialog.setRightButton(getString(R.string.stringBtnYes),View.OnClickListener {
                mainViewModel.route.value = null
                dialog.dismiss()
            })
            dialog.setLeftButton(getString(R.string.stringBtnNo),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.show(fragmentManager!!,"dialog")
        }
    }


    private fun createRecordRouteStartDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_recordStart))
            dialog.setMessage(getString(R.string.dialogMessage_recordStart))
            dialog.setRightButton(getString(R.string.stringBtnClose),View.OnClickListener {
                val intent = Intent(context, RecordRouteService::class.java)
                context!!.startService(intent)
                dialog.dismiss()
            })
            dialog.setLeftButton(getString(R.string.stringBtnStart),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.show(fragmentManager!!,"dialog")
        }
    }

    private fun createRecordRouteStopDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_routeRecordStop))
            dialog.setMessage(getString(R.string.dialogMessage_routeRecordStop))
            dialog.setLeftButton(getString(R.string.stringBtnNo),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.setRightButton(getString(R.string.stringBtnYes),View.OnClickListener {
                val intent = Intent(context, RecordRouteService::class.java)
                context!!.stopService(intent)
                dialog.dismiss()
            })

            dialog.setButtonStyle(SimpleDialog.STYLE_DANGER)
            dialog.show(fragmentManager!!,"dialog")
        }
    }

    private fun createGroupRidingStartDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_groupRidingStart))
            dialog.setMessage(getString(R.string.dialogMessage_groupRidingStart))
            dialog.setLeftButton(getString(R.string.stringBtnJoin),View.OnClickListener {
                createGroupRidingJoinDialog()
                dialog.dismiss()
            })
            dialog.setRightButton(getString(R.string.stringBtnCreate),View.OnClickListener {
                val intent = Intent(context, GroupRidingService::class.java)
                intent.putExtra("RequestCreateGroup",true)
                context!!.startService(intent)
                dialog.dismiss()
            })

            dialog.setButtonStyle(SimpleDialog.STYLE_NORMAL)
            dialog.show(fragmentManager!!,"dialog")
        }
    }

    private fun createGroupRidingJoinDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_groupRidingJoin))
            dialog.setMessage(getString(R.string.dialogMessage_groupRidingJoin))
            dialog.setEditText("CODE")
            dialog.setLeftButton(getString(R.string.stringBtnClose),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.setRightButton(getString(R.string.stringBtnJoin),View.OnClickListener {
                val intent = Intent(context, GroupRidingService::class.java)
                intent.putExtra("RequestCreateGroup",false)
                SettingRepository.inviteCode.value = dialog.viewModel.editText.value
                context!!.startService(intent)
                dialog.dismiss()
            })
            dialog.show(fragmentManager!!,"dialog")
        }
    }

    private fun createGroupRidingStopDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_groupRidingStop))
            dialog.setMessage(getString(R.string.dialogMessage_groupRidingStop))
            dialog.setLeftButton(getString(R.string.stringBtnNo),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.setRightButton(getString(R.string.stringBtnYes),View.OnClickListener {
                val intent = Intent(context, GroupRidingService::class.java)
                context!!.stopService(intent)
                dialog.dismiss()
            })

            dialog.setButtonStyle(SimpleDialog.STYLE_DANGER)
            dialog.show(fragmentManager!!,"dialog")
        }
    }

    private fun createGroupRidingInviteDialog(){
        SimpleDialog.getInstance().also { dialog->
            dialog.setTitle(getString(R.string.dialogTitle_groupRidingInvite))
            dialog.setMessage(getString(R.string.dialogMessage_groupRidingInvite))
            dialog.setBigMessage(SettingRepository.inviteCode.value!!)
            dialog.setLeftButton(getString(R.string.stringBtnClose),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.setRightButton(getString(R.string.stringBtnShare),View.OnClickListener {
                dialog.dismiss()
            })
            dialog.show(fragmentManager!!,"dialog")
        }
    }

    inner class GroupCreateCompletedBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            createGroupRidingInviteDialog()
        }
    }
}

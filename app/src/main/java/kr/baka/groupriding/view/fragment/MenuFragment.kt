package kr.baka.groupriding.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.baka.groupriding.R
import kr.baka.groupriding.databinding.FragmentMenuBinding
import kr.baka.groupriding.databinding.FragmentMenuBindingImpl
import kr.baka.groupriding.view.activity.SettingActivity
import kr.baka.groupriding.view.dialog.AskGroupRidingStartDialog
import kr.baka.groupriding.view.dialog.AskGroupRidingStopDialog
import kr.baka.groupriding.view.dialog.GroupCodeShowDialog
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
            AskGroupRidingStartDialog(context!!).show()
        })

        viewModel.stopGroupRidingServiceEvent.observe(this, Observer {
            AskGroupRidingStopDialog(context!!).show()
        })

        viewModel.inviteCodeDialogShowEvent.observe(this, Observer {
            GroupCodeShowDialog(context!!).show()
        })

        viewModel.showSettingActivityEvent.observe(this, Observer {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        })

        return binding.root
    }

}

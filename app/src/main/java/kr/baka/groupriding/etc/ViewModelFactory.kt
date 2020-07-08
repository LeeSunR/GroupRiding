package kr.baka.groupriding.etc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.baka.groupriding.viewmodel.MainViewModel
import kr.baka.groupriding.viewmodel.MenuViewModel

class ViewModelFactory :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return hashMapViewModel.getOrPut(modelClass.simpleName) {MainViewModel()} as T

        if (modelClass.isAssignableFrom(MenuViewModel::class.java))
            return hashMapViewModel.getOrPut(modelClass.simpleName) {MenuViewModel()} as T




        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        val hashMapViewModel = HashMap<String, ViewModel>()
//        fun addViewModel(key: String, viewModel: ViewModel){
//            hashMapViewModel[key] = viewModel
//        }
//        fun getViewModel(key: String): ViewModel? {
//            return hashMapViewModel[key]
//        }
    }
}
package kr.baka.groupriding.repository

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.baka.groupriding.etc.App

object ServiceStatusLiveData{
    val groupingService:MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>().also { it.value=false } }
    val recordingService:MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>().also { it.value=false } }
}
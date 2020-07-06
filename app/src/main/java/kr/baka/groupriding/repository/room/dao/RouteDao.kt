package kr.baka.groupriding.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kr.baka.groupriding.repository.room.entity.RouteEntity

@Dao
interface RouteDao{

    @Insert fun insert(routeEntity: RouteEntity)

    @Query("SELECT * FROM route")
    fun getAllRoute(): LiveData<List<RouteEntity>>

    @Query("SELECT MAX(rid) FROM route")
    fun getLastRoute(): Int

    @Query("DELETE FROM route WHERE rid = :rid")
    fun delete(rid: Int)
}
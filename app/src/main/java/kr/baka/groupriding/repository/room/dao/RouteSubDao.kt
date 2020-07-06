package kr.baka.groupriding.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity

@Dao
interface RouteSubDao{

    @Insert fun insert(routeSubEntity: RouteSubEntity)

    @Insert fun insert(list : List<RouteSubEntity>)

    @Query("SELECT * FROM routeSub WHERE rid = :rid")
    fun getRouteSub(rid: Int): LiveData<List<RouteSubEntity>>

    @Query("DELETE FROM routeSub WHERE rid = :rid")
    fun delete(rid: Int)

}
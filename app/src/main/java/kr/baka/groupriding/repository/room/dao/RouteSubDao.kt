package kr.baka.groupriding.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import kr.baka.groupriding.repository.room.entity.RouteSubEntity

@Dao
interface RouteSubDao{

    @Insert fun insert(routeSubEntity: RouteSubEntity)


    @Insert fun insert(list : List<RouteSubEntity>)

}
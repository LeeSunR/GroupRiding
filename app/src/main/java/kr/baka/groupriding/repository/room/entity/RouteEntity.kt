package kr.baka.groupriding.repository.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "route")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true) var rid: Int = 0
    , @ColumnInfo(name = "date") var date: Long
    , @ColumnInfo(name = "distance") var distance: Int
    , @ColumnInfo(name = "name") var name: String = ""
)
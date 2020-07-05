package kr.baka.groupriding.repository.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "routeSub")
data class RouteSubEntity(
    @PrimaryKey(autoGenerate = true) var rsubid: Int = 0
    , @ForeignKey(entity = RouteEntity::class
                    , parentColumns = ["rid"]
                    , childColumns = ["rid"]
                    , onDelete = ForeignKey.CASCADE) var rid: Int = 0
    , @ColumnInfo(name = "timestamp") var timestamp: Long
    , @ColumnInfo(name = "latitude") var latitude: Double
    , @ColumnInfo(name = "longitude") var longitude: Double
)
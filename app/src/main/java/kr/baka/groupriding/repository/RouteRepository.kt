package kr.baka.groupriding.repository

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import kr.baka.groupriding.etc.App.Companion.context
import kr.baka.groupriding.repository.room.AppDatabase
import kr.baka.groupriding.repository.room.dao.RouteDao
import kr.baka.groupriding.repository.room.dao.RouteSubDao
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity

class RouteRepository(var context: Context) {

    companion object {
        private class insertAsyncTask constructor(private val asyncTaskDao: RouteDao) : AsyncTask<RouteEntity, Void, Void>() {
            override fun doInBackground(vararg params: RouteEntity): Void? {
                asyncTaskDao.insert(params[0])
                return null
            }
        }

        private class InsertRouteTask constructor(
            private val db: AppDatabase,
            private val routeDao: RouteDao,
            private val routeSubDao: RouteSubDao) : AsyncTask<Pair<RouteEntity, List<RouteSubEntity>>, Void, Void>() {
            override fun doInBackground(vararg params: Pair<RouteEntity, List<RouteSubEntity>>?): Void? {

                val routeEntity = params[0]!!.first
                val list = params[0]!!.second
                db.runInTransaction {
                    routeDao.insert(routeEntity)
                    val rid = routeDao.getLastRoute()
                    for (i in list.indices){
                        list[i].rid = rid
                    }
                    routeSubDao.insert(list)
                }
                return null
            }


        }
    }


    private val db = AppDatabase.getInstance(context.applicationContext)
    private val routeDao: RouteDao = db.routeDao()
    private val routeSubDao: RouteSubDao = db.routeSubDao()


    val allRoute = db.routeDao().getAllRoute()

    fun insert(routeEntity: RouteEntity) {
        insertAsyncTask(routeDao).execute(routeEntity)
    }

    fun insert(routeEntity: RouteEntity, list : List<RouteSubEntity>){
        InsertRouteTask(db,routeDao,routeSubDao).execute(Pair(routeEntity,list))
    }

}
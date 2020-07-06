package kr.baka.groupriding.repository

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import kr.baka.groupriding.etc.App.Companion.context
import kr.baka.groupriding.repository.room.AppDatabase
import kr.baka.groupriding.repository.room.dao.RouteDao
import kr.baka.groupriding.repository.room.dao.RouteSubDao
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity

class RouteRepository {

    companion object {
        private class InsertAsyncTask constructor(private val asyncTaskDao: RouteDao) : AsyncTask<RouteEntity, Void, Void>() {
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

        private class DeleteRouteTask constructor(
            private val db: AppDatabase,
            private val routeDao: RouteDao,
            private val routeSubDao: RouteSubDao) : AsyncTask<RouteEntity, Void, Void>() {
            override fun doInBackground(vararg params: RouteEntity): Void? {
                db.runInTransaction {
                    routeSubDao.delete(rid = params[0].rid)
                    routeDao.delete(rid = params[0].rid)
                }
                return null
            }
        }
    }


    private val db:AppDatabase
    private val routeDao: RouteDao
    private val routeSubDao: RouteSubDao
    private val allRoute:LiveData<List<RouteEntity>>

    constructor(context: Context) {
        db = AppDatabase.getInstance(context)
        routeDao = db.routeDao()
        routeSubDao = db.routeSubDao()
        allRoute = db.routeDao().getAllRoute()
    }

    fun getAllRoute(): LiveData<List<RouteEntity>> {
        return allRoute
    }

    fun insert(routeEntity: RouteEntity) {
        InsertAsyncTask(routeDao).execute(routeEntity)
    }

    fun insert(routeEntity: RouteEntity, list : List<RouteSubEntity>){
        InsertRouteTask(db,routeDao,routeSubDao).execute(Pair(routeEntity,list))
    }

    fun delete(routeEntity: RouteEntity){
        DeleteRouteTask(db,routeDao,routeSubDao).execute(routeEntity)
    }
}
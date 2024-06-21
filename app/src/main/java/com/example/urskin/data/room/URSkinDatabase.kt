package com.example.urskin.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [HistoryEntity::class], version = 1)
abstract class URSkinDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object{
        @Volatile
        private var INSTANCE: URSkinDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context):URSkinDatabase{
            if (INSTANCE == null){
                synchronized(URSkinDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        URSkinDatabase::class.java,"urskin_database")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as URSkinDatabase
        }
    }
}
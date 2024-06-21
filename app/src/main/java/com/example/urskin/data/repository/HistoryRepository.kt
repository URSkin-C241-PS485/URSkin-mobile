package com.example.urskin.data.repository

import android.app.Application
import com.example.urskin.data.room.HistoryEntity
import com.example.urskin.data.room.URSkinDatabase

class HistoryRepository(application: Application) {
    private val historyDao= URSkinDatabase.getDatabase(application).historyDao()

    suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity)
    }

    fun getHistory(): List<HistoryEntity> = historyDao.getHistory()
}
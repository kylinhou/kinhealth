package com.kinhealth.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemberEntity::class, HealthRecordEntity::class], version = 1, exportSchema = false)
abstract class KinHealthDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun healthRecordDao(): HealthRecordDao

    companion object {
        @Volatile
        private var INSTANCE: KinHealthDatabase? = null

        fun getDatabase(context: Context): KinHealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KinHealthDatabase::class.java,
                    "kin_health.db"
                )
                // 纯净初态：不再内置任何假数据人员
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

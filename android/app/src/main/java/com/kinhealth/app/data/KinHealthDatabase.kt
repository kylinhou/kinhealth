package com.kinhealth.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MemberEntity::class, HealthRecordEntity::class], version = 1, exportSchema = false)
abstract class KinHealthDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun healthRecordDao(): HealthRecordDao

    companion object {
        @Volatile
        private var INSTANCE: KinHealthDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): KinHealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KinHealthDatabase::class.java,
                    "kin_health.db"
                )
                .addCallback(KinHealthDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class KinHealthDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.memberDao(), database.healthRecordDao())
                }
            }
        }

        private suspend fun populateInitialData(memberDao: MemberDao, recordDao: HealthRecordDao) {
            val members = listOf(
                MemberEntity(
                    id = "baby",
                    name = "安安",
                    avatar = "👶",
                    roleText = "宝宝",
                    ageText = "8个月",
                    weightKg = 8.6,
                    currentTag = "发热监护中",
                    hasFeverAlarm = true,
                    allergiesCsv = "鸡蛋清 (轻度湿疹), 青霉素 (红线禁忌)"
                ),
                MemberEntity(
                    id = "grandpa",
                    name = "张大爷",
                    avatar = "👴",
                    roleText = "长辈",
                    ageText = "68岁",
                    weightKg = 72.0,
                    currentTag = "痛风慢病管理",
                    hasFeverAlarm = false,
                    allergiesCsv = "痛风期禁用阿司匹林"
                ),
                MemberEntity(
                    id = "mom",
                    name = "林女士",
                    avatar = "👩",
                    roleText = "母亲",
                    ageText = "32岁",
                    weightKg = 54.0,
                    currentTag = "健康良好",
                    hasFeverAlarm = false,
                    allergiesCsv = "无已知药物过敏"
                )
            )
            memberDao.insertAll(members)

            val now = System.currentTimeMillis()
            // 安安的体温记录与用药记录
            recordDao.insertRecord(
                HealthRecordEntity(
                    memberId = "baby",
                    timestamp = now - 3600 * 4 * 1000,
                    type = "TEMPERATURE",
                    valuePrimary = "38.8 ℃",
                    notes = "耳温，精神稍显烦躁",
                    isAlert = true
                )
            )
            recordDao.insertRecord(
                HealthRecordEntity(
                    memberId = "baby",
                    timestamp = now - 3600 * 3 * 1000,
                    type = "MEDICATION",
                    valuePrimary = "泰诺林 (对乙酰氨基酚)",
                    valueSecondary = "3.5ml",
                    notes = "服药后半小时顺利降温",
                    isAlert = false
                )
            )
            // 张大爷的尿酸记录
            recordDao.insertRecord(
                HealthRecordEntity(
                    memberId = "grandpa",
                    timestamp = now - 3600 * 2 * 1000,
                    type = "URIC_ACID",
                    valuePrimary = "485 μmol/L",
                    notes = "超出安全基线 420，需大量饮水",
                    isAlert = true
                )
            )
        }
    }
}


package com.example.enursery.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.enursery.core.data.source.local.entity.BarisEntity
import com.example.enursery.core.data.source.local.entity.BatchEntity
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.UserEntity
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity

@Database(
    entities = [
        UserEntity::class,
        RoleEntity::class,
        WilayahKerjaEntity::class,
        PlotEntity::class,
        VgmEntity::class,
        BatchEntity::class,
        VgmHistoryEntity::class,
        BarisEntity::class],
    version = 19,
    exportSchema = false
)
@TypeConverters(DateConverter::class, LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun roleDao(): RoleDao
    abstract fun wilayahDao(): WilayahKerjaDao
    abstract fun plotDao(): PlotDao
    abstract fun vgmDao(): VgmDao
    abstract fun batchDao(): BatchDao
    abstract fun vgmHistoryDao(): VgmHistoryDao
    abstract fun barisDao(): BarisDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "App.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

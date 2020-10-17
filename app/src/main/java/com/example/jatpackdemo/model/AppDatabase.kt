package com.example.jatpackdemo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 3, entities = [User::class, Book::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao

    companion object {
        private var instance: AppDatabase? = null

        // 数据库版本由1升级到2 新增表Book
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建Book表
                database.execSQL("create table book (id integer primary key autoincrement not null, name text not null, pages integer not null)")
            }
        }

        // 数据库版本由2升级到3 Book表新增字段作者
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建Book表
                database.execSQL("alter table Book add column author text not null default 'unknown'")
            }
        }

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                 AppDatabase::class.java, "app_database")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build().apply {
                    instance = this
                }
        }
    }
}
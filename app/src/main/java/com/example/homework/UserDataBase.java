package com.example.homework;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.homework.Model.User;

@Database(entities = {User.class},version = 4)

public abstract class UserDataBase extends RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE 'user' ADD COLUMN 'checked_val' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE 'user' ADD COLUMN 'pro_image' TEXT");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE 'user' ADD COLUMN 'device_uuid' TEXT");
        }
    };

    private static UserDataBase dataBase;
    public abstract UserDAO getUserDao();
    public static UserDataBase getDataBase(Context context){

        if (dataBase==null){
            dataBase = Room.databaseBuilder(context,UserDataBase.class,"UserDb")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4)
                    .build();
        }

        return dataBase;
    }


}

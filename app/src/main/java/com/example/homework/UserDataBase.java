package com.example.homework;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.homework.Model.User;

@Database(entities = {User.class},version = 1,exportSchema = false)

public abstract class UserDataBase extends RoomDatabase {

    public abstract UserDAO getUserDao();
}

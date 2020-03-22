package com.example.homework;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.homework.Model.User;

@Dao
public interface UserDAO {

    @Insert
    public long addUser(User user);


}

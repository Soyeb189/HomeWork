package com.example.homework;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.homework.Model.User;

@Dao
public interface UserDAO {

    @Insert
    public long addUser(User user);

    @Update
    public void updateUser(User user);

    @Delete
    public void deleteUser(User user);

    @Query("SELECT * FROM User where user_email= :mail and user_password= :password")
    User getUser(String mail, String password);

    @Query("SELECT * FROM User where user_id = :id")
    User getSelectedUser(long id);

    @Query("UPDATE user SET user_name = :name , user_email = :email WHERE user_id = :u_id")
    void updateSelected(String name,String email,long u_id);


}

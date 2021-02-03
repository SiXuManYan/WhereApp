package com.jcs.where.storage.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jcs.where.storage.entity.User;

/**
 * Created by Wangsw  2021/2/3 11:31.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User var1);

    @Query("SELECT * FROM table_user")
    User findUser();

    @Query("DELETE FROM table_user")
    void clear();

    @Update
    void updateUser(User user);

}

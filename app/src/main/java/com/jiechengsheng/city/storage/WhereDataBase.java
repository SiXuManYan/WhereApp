package com.jiechengsheng.city.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.jiechengsheng.city.storage.dao.UserDao;
import com.jiechengsheng.city.storage.entity.User;

/**
 * Created by Wangsw  2021/2/3 10:50.
 */
@Database(
        entities = {User.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class WhereDataBase extends RoomDatabase {

    private static final String DatabaseFileName = "Where.db";
    private static WhereDataBase instance;

    public static synchronized WhereDataBase get(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, WhereDataBase.class, DatabaseFileName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }


   public abstract UserDao userDao();


}

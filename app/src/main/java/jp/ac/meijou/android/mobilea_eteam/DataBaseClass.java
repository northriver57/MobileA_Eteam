package jp.ac.meijou.android.mobilea_eteam;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Room DataBaseクラス
 */
@Database(entities = {DataRoom.class}, version = 1, exportSchema = false)
public abstract class DataBaseClass extends RoomDatabase {
    public abstract DaoClass daoClass();
}
package jp.ac.meijou.android.mobilea_eteam;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Room DataBaseクラス
 */
@Database(entities = {DataRoom.class}, version = 3, exportSchema = false)
public abstract class DataBaseClass extends RoomDatabase {
    public abstract DaoClass daoClass();

/*
*     // マイグレーションを追加
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
// 一時的な新しいカラムを作成
            database.execSQL("ALTER TABLE data_room ADD COLUMN classification_temp TEXT");

            // 既存のデータを新しいカラムにコピー
            database.execSQL("UPDATE data_room SET classification_temp = classification");

            // 既存のカラムを削除
            database.execSQL("CREATE TABLE IF NOT EXISTS data_room_new (id INTEGER PRIMARY KEY NOT NULL, classification TEXT, asset TEXT NOT NULL, price INTEGER NOT NULL, content TEXT, date INTEGER NOT NULL)");
            database.execSQL("INSERT INTO data_room_new (id, classification, asset, price, content, date) SELECT id, classification_temp, asset, price, content, date FROM data_room");
            database.execSQL("DROP TABLE data_room");
            database.execSQL("ALTER TABLE data_room_new RENAME TO data_room");
        }
    };
* */

}
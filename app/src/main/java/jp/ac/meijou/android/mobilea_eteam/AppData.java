package jp.ac.meijou.android.mobilea_eteam;

import android.app.Application;
import android.content.Context;
import androidx.room.Room;

public class AppData extends Application {
    private static DataBaseClass database;
    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), DataBaseClass.class, "budget_database")
                .build();

    }
    public static DataBaseClass getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), DataBaseClass.class, "budget_database")
                    .build();
        }
        return database;
    }
}
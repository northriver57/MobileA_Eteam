package jp.ac.meijou.android.mobilea_eteam;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.Calendar;
import java.util.List;

public class RecordViewModel extends AndroidViewModel {

    private DaoClass dao;
    private LiveData<List<DataRoom>> allData;

    public RecordViewModel(Application application) {
        super(application);
        dao = AppData.getDatabase(application).daoClass();
        allData = dao.getAllData();
    }

    public LiveData<List<DataRoom>> getAllData() {
        return allData;
    }

    public void insertData(String classification, String asset, int price, String content, long date, int type ) {
        new InsertAsyncTask(dao).execute(new DataRoom(classification, asset, price, content, date, type));
    }

    private static class InsertAsyncTask extends AsyncTask<DataRoom, Void, Void> {
        private DaoClass asyncTaskDao;

        InsertAsyncTask(DaoClass dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DataRoom... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

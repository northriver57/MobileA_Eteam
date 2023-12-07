package jp.ac.meijou.android.mobilea_eteam;
import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class RecordViewModel extends AndroidViewModel {

    private final DaoClass dao;
    private final LiveData<List<DataRoom>> allData;

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

    public void deleteData(long itemId) {
        new DeleteAsyncTask(dao).execute(itemId);
    }
    private static class InsertAsyncTask extends AsyncTask<DataRoom, Void, Void> {
        private final DaoClass asyncTaskDao;

        InsertAsyncTask(DaoClass dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DataRoom... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }


    // 他のメソッドと同様にLiveDataを返すメソッドを作成
    public LiveData<List<DataRoom>> getDataByYearMonth(String yearMonth) {
        return dao.getDataByYearMonth(yearMonth);
    }

    private static class DeleteAsyncTask extends AsyncTask<Long, Void, Void> {
        private final DaoClass asyncTaskDao;

        DeleteAsyncTask(DaoClass dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            asyncTaskDao.deleteData(params[0]);
            return null;
        }

    }
}

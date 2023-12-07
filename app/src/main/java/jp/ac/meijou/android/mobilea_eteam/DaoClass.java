package jp.ac.meijou.android.mobilea_eteam;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Room(SQLite)用データベース操作インターフェース
 */
@Dao
public interface DaoClass {

    /**
     * データを追加
     * @param ad 追加データ
     */
    @Insert
    void insert(DataRoom ad);

    /**
     * データを更新a
     * @param ad 更新データ
     */
    @Update
    void update(DataRoom ad);


    @Query("SELECT * FROM data_room ORDER BY date ASC")
    LiveData<List<DataRoom>> getAllData();


    // 特定年月のデータを取得するクエリ
    @Query("SELECT * FROM data_room WHERE strftime('%Y-%m', date / 1000, 'unixepoch') = :yearMonth")
    LiveData<List<DataRoom>> getDataByYearMonth(String yearMonth);

    @Query("DELETE FROM data_room WHERE id = :itemId")
    void deleteData(long itemId);



}
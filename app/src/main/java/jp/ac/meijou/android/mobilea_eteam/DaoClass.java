package jp.ac.meijou.android.mobilea_eteam;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Room(SQLite)用データベース操作インターフェース
 */
@Dao
public interface DaoClass {
    /**
     * 指定した期間のデータを取得
     * @param startDate 表示開始日
     * @param lastDate  表示終了日
     * @return 家計簿データリスト
     */
    @Query("SELECT * FROM data_room WHERE date > :startDate AND date < :lastDate ORDER BY date ASC")
    List<DataRoom> getData(long startDate, long lastDate);

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

    /**
     * データを削除
     * @param ad 削除データ
     */
    @Delete
    void delete(DataRoom ad);

    @Query("SELECT * FROM data_room ORDER BY date ASC")
    LiveData<List<DataRoom>> getAllData();

    @Query("DELETE FROM data_room WHERE id = :itemId")
    void deleteData(long itemId);


}
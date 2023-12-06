package jp.ac.meijou.android.mobilea_eteam;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 家計簿データクラス（Room）
 */
@Entity(tableName = "data_room") //テーブル名を定義
public class DataRoom{

    @PrimaryKey(autoGenerate = true)
    public int id; //主キー
    public String classification;//「支出分類」カラムを定義
    public String asset;       //「資産の種類」カラムを定義
    public int price;        //「金額」カラムを定義
    public String content;   //「内容」カラムを定義
    public long date;        //「日付」カラムを定義
    public int type;
    /**
     * コンストラクタ
     * @param classification 支出分類
     * @param asset   資産の種類
     * @param price   金額
     * @param content 内容
     * @param date    日付
     * @param type 収入，支出の分類
     */

    public DataRoom(String classification, String asset, int price, String content, long date, int type) {
        this.classification = classification;
        this.asset = asset;      //「資産の種類」を設定
        this.price = price;      //「金額」を設定
        this.content = content;  //「内容」を設定
        this.date = date;        //「日付」を設定
        this.type = type;
    }

    public int getId(){
        return id;
    }

    /**
     * 「支出分類」を取得（Getter）
     * @return classification
     */
    public String getClassification() {

        return classification;
    }

    /**
    * 「資産の種類」を取得（Getter）
     * @return asset
    */
    public String getAsset() {
        return asset;
    }

    /**
     * 「価格」を取得（Getter）
     * @return price
     */
    public int getPrice() {
        return price;
    }

    /**
     * 「内容」を取得（Getter）
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     *　「日付」を取得（Getter）
     * @return date
     */
    public long getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    /**
     * 「家計簿データ」を更新
     * @param classification 更新する「支出分類」
     * @param content 更新する「内容」
     * @param asset   更新する「資産の種類」
     * @param price   更新する「金額」
     * @param date    更新する「日付」
     * @param type
     * @return 更新した「家計簿データ」
     */

    public DataRoom update(String classification, String asset, int price, String content, long date, int type) {
        this.classification = classification;
        this.asset = asset;      //「資産の種類」を設定
        this.price = price;      //「金額」を設定
        this.content = content;  //「内容」を設定
        this.date = date;        //「日付」を設定
        this.type = type;
        return this;
    }
}
package jp.ac.meijou.android.mobilea_eteam;

import android.content.Context;
import android.content.Intent;

public class ButtonClickListener {
    private final Context context;

    public ButtonClickListener(Context context) {
        this.context = context;
    }

    public void onButtonClick(Class<?> destinationActivity) {
        // ボタンがクリックされたときの共通の動作
        Intent intent = new Intent(context, destinationActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    // ボタン4がクリックされたときの動作
    public void onButton4Click() {
        onButtonClick(totalActivity.class);
    }

    // ボタン3がクリックされたときの動作
    public void onButton3Click() {
        onButtonClick(LineActivity.class);
    }

    // ボタン2がクリックされたときの動作
    public void onButton2Click() {
        onButtonClick(PieActivity.class);
    }

    // ボタン6がクリックされたときの動作
    public void onButton6Click() {
        onButtonClick(PlusActivity.class);
    }



}

package br.com.memocellcio.db;

import android.content.Context;

import io.objectbox.BoxStore;

public class DBFactory {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get() {
        return boxStore;
    }

    public static void setBoxStore(BoxStore boxStore) {
        DBFactory.boxStore = boxStore;
    }
}

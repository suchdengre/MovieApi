package com.demo.moviedemo.constant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.moviedemo.R;
import com.demo.moviedemo.network.ApiModels.Search;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    public static final String BOOKMAR_LIST = "bookmark_list";
    public final static int PREFTYPE_STRING = 2;

    public static void saveStringPreferences(Context context, String strKey, String strValue) {
        try {
            if (context != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (strValue != null)
                    editor.putString(strKey, strValue);
                else
                    editor.putString(strKey, "");
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getPreferences(Context context, String key, int preferenceDataType) {
        Object value = null;
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            switch (preferenceDataType) {
                case PREFTYPE_STRING:
                    value = sharedPreferences.getString(key, "");
                    break;

            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
            switch (preferenceDataType) {
                case PREFTYPE_STRING:
                    value = "";
                    break;
            }
        }

        return value;
    }

    public static ArrayList<Search> getBookMarkedList(Context context) {
        ArrayList<Search> bookmarkList = new ArrayList<>();
        Gson gson = new Gson();
        String bookMarkedString = (String) Utils.getPreferences(context, Utils.BOOKMAR_LIST, Utils.PREFTYPE_STRING);
        if (!TextUtils.isEmpty(bookMarkedString)) {
            Type type = new TypeToken<ArrayList<Search>>() {
            }.getType();
            bookmarkList = gson.fromJson(bookMarkedString, type);
        }
        return bookmarkList;
    }

    public static void checkIsBookMarked(Context mContext, Search search) {
        ArrayList<Search> bookMarkList = getBookMarkedList(mContext);
        for (Search searchModel : bookMarkList) {
            if (searchModel.getImdbID().equalsIgnoreCase(search.getImdbID()))
                search.setBookmarked(true);
        }
    }

    public static void loadImage(Context context,ImageView imageView, String poster) {
        Glide.with(context)  //2
                .load(poster) //3
                .centerCrop() //4
                .placeholder(R.drawable.ic_launcher_background) //5
                .error(R.mipmap.ic_launcher) //6
                .into(imageView); //7
    }

    public static void saveBookmarkedList(Context mContext, Search search) {
        ArrayList<Search> bookmarkList = Utils.getBookMarkedList(mContext);
        Gson gson = new Gson();
        bookmarkList.add(search);
        String json = gson.toJson(bookmarkList);
        Utils.saveStringPreferences(mContext, Utils.BOOKMAR_LIST, json);
    }

    public static void removeBookmarkedList(Context mContext, Search search) {
        ArrayList<Search> bookmarkList = Utils.getBookMarkedList(mContext);
        removeFromBookmark(mContext,search.getImdbID(),bookmarkList);

    }

    private static void removeFromBookmark(Context context,String imdbID, ArrayList<Search> bookmarkList) {
        for (int i = 0; i < bookmarkList.size(); i++) {
            if (bookmarkList.get(i).getImdbID().equalsIgnoreCase(imdbID))
                bookmarkList.remove(i);
        }
        Gson gson = new Gson();
        String json = gson.toJson(bookmarkList);
        Utils.saveStringPreferences(context, Utils.BOOKMAR_LIST, json);
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
}

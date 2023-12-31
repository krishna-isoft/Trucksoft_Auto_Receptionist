package com.isoft.trucksoft_autoreceptionist;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by isoft on 27/12/17.
 */

public class Font_manager {
    private  static Hashtable<String, Typeface> font_icons=new Hashtable<>();
    public static Typeface get_icons(String path, Context context)
    {
        Typeface icons=font_icons.get(path);
        if(icons==null)
        {
            icons= Typeface.createFromAsset(context.getAssets(),path);
            font_icons.put(path,icons);
        }
        return  icons;
    }
}

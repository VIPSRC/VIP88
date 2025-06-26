package pubgm.loader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import java.util.Locale;


public class myTools {
	SharedPreferences sp,sp2;
	public Context ctx;
	public myTools(Context ctx){
		this.ctx = ctx;
		
	}
	public void setBool(String file,String map, boolean write) {
		sp = ctx.getSharedPreferences(file,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(map, write);
        ed.apply();
		ed.commit();
    }
	public boolean getBool(String file,String map,boolean ori) {
		sp = ctx.getSharedPreferences(file,Context.MODE_PRIVATE);
        return sp.getBoolean(map, ori);
    }
	
	public void setInt(String file,String map, int write) {
		sp = ctx.getSharedPreferences(file,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(map, write);
        ed.apply();
		ed.commit();
    }
	public int geInt(String file,String map,int ori) {
		sp = ctx.getSharedPreferences(file,Context.MODE_PRIVATE);
        return sp.getInt(map, ori);
    }
	
	public void setSt(String file,String map, String write) {
		sp = ctx.getSharedPreferences(file,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(map, write);
        ed.apply();
		ed.commit();
    }
   public String getSt(String file,String map,String ori) {
	   sp = ctx.getSharedPreferences(file,Context.MODE_PRIVATE);
        return sp.getString(map, ori);
    }



    public void setLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        // Create a configuration object and set the locale
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }



    public int col(int attr){
		TypedValue typedValue = new TypedValue();
		ctx.getTheme().resolveAttribute(attr,typedValue,true);
		return ContextCompat.getColor(ctx,typedValue.resourceId);
	}
	
	
}



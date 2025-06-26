package pubgm.loader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import androidx.core.content.ContextCompat;
import java.util.Locale;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import pubgm.loader.R;
//import android.support.v4.content.ContextCompat;


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
	
	
    
	
	public void setLocale(Activity act, String cd) {
        Locale loc = new Locale(cd);
        loc.setDefault(loc);
         Resources ress = act.getResources();
        Configuration cfg = ress.getConfiguration();
        cfg.setLocale(loc);
        ress.updateConfiguration(cfg, ress.getDisplayMetrics());
    }
	
	public int col(int attr){
		TypedValue typedValue = new TypedValue();
		ctx.getTheme().resolveAttribute(attr,typedValue,true);
		return ContextCompat.getColor(ctx,typedValue.resourceId);
	}
	
	
}



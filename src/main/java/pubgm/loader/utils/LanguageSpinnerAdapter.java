package pubgm.loader.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LanguageSpinnerAdapter extends ArrayAdapter<LanguageItem> {
    private Context context;
    private List<LanguageItem> languageList;

    public LanguageSpinnerAdapter(Context context, int simple_spinner_item, List<LanguageItem> languageList) {
        super(context, android.R.layout.simple_spinner_item, languageList);
        this.context = context;
        this.languageList = languageList;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(languageList.get(position).getName());
        view.setTextColor(Color.BLACK);
        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(languageList.get(position).getName());
        return view;
    }
}

package com.schibsted.spain.barista.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewActivity extends AppCompatActivity {

  private static final String[] FRUITS = {"Banana", "Apple", "Orange", "Raspberry"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listview);

    ListView listView = (ListView) findViewById(R.id.listview);
    listView.setAdapter(new TextAdapter(this, FRUITS));
  }

  public static class TextAdapter extends BaseAdapter {
    private Activity activity;
    private String[] items;

    TextAdapter(Activity activity, String[] items) {
      this.activity = activity;
      this.items = items.clone();
    }

    @Override
    public int getCount() {
      return this.items.length;
    }

    @Override
    public Object getItem(int position) {
      return this.items[position];
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View rowView = convertView;

      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.row_with_buttons, parent, false);
      }

      final TextView textView = (TextView) rowView.findViewById(R.id.textview);
      textView.setText(items[position]);

      rowView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent i = new Intent(activity, LabelActivity.class);
          i.putExtra(LabelActivity.EXTRA_TEXT, textView.getText().toString());
          activity.startActivity(i);
        }
      });

      return rowView;
    }
  }
}

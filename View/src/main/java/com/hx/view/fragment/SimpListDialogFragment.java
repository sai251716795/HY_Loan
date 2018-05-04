package com.hx.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hx_view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administor on 2017/10/13.
 */

public  class SimpListDialogFragment extends DialogFragment {
    private ListView listView;
    private ImageView cancel;
    private TextView titleText;
    private String title;
    private List<String> data = new ArrayList<>();
    Context context;

    private float aFloat;
    public SimpListDialogFragment(Context context, String title, List<String> data) {
        this.title = title;
        this.context = context;
        this.data = data;
    }

    public SimpListDialogFragment setTextSize(float aFloat){
        this.aFloat = aFloat;
        return this;
    }
    public interface  ItemClickListener{
       void  onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.simp_list, null);
        view.setBackgroundColor(0x00ffffff);
        listView = (ListView) view.findViewById(R.id.simple_list);
        titleText = (TextView) view.findViewById(R.id.title);
        cancel = (ImageView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (title == null)
            title = "列表";
        titleText.setText(title);
        builder.setView(view);

        simpleListAdapter simpleListAdapter = new simpleListAdapter(data, context);
        listView.setAdapter(simpleListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(itemClickListener!= null){
                    itemClickListener.onItemClick(parent,view,position,id);
                }
            }
        });
        return builder.create();
    }
    private void initView(View view) {

    }

    class simpleListAdapter extends BaseAdapter {

        private List<String> data = new ArrayList<>();

        Context context;

        public simpleListAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.simple_text_center_item, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text_center);
                setTextView(viewHolder);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String detail = data.get(position);
            viewHolder.textView.setText(detail);
            return convertView;
        }

        private void setTextView( ViewHolder viewHolder){
            viewHolder.textView.setTextSize(aFloat);
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
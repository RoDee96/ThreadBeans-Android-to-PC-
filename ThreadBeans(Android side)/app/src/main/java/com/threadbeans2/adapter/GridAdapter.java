package com.threadbeans2.adapter;

import android.support.design.widget.Snackbar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.threadbeans2.R;
import com.threadbeans2.activity.MainActivity;
import com.threadbeans2.beans.GridItemBeans;

import java.io.File;
import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;


/**
 * Created by Rohit on 7/6/2017.
 */

public class GridAdapter extends ArrayAdapter {
    //static boolean itemCheckedStatus[];

    private final Context context;
    private final int resource;
    private final ArrayList<GridItemBeans> objects;
    private LayoutInflater inflater;

    public GridAdapter(Context context, int resource, ArrayList<GridItemBeans> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;


        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(resource, null);

        Log.d("gridView", "GridAdapter: ObjectSize = "+objects.size());
//        itemCheckedStatus = new boolean[objects.size()];

        CheckBox cbox = (CheckBox) view.findViewById(R.id.checkbox);

        if(objects.get(position).isChecked()){
            cbox.setChecked(true);
            cbox.setSelected(true);
            //itemCheckedStatus[position] = true;
            Log.d("gridView", "getView: "+objects.get(position).isChecked());
        }

        cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.items.get(position).setChecked(true);
              //      itemCheckedStatus[position] = true;
                    MainActivity.countSelected ++;
                    Snackbar.make(buttonView, "Selected: " + MainActivity.countSelected, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else {
                //    itemCheckedStatus[position] = false;
                    MainActivity.items.get(position).setChecked(false);
                    MainActivity.countSelected --;
                    Snackbar.make(buttonView, "Selected: " + MainActivity.countSelected, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                Log.d("456", "onCheckedChanged: "+ MainActivity.items.get(position).isChecked());
                //Log.d("456", "onCheckedChanged: "+itemCheckedStatus[position]);
            }
        });

        TextView filename = (TextView) view.findViewById(R.id.fileName);
        ImageView imgView = (ImageView) view.findViewById(R.id.file_icon);

        filename.setText(objects.get(position).getFilename());
        if(objects.get(position).is_path()) {
            imgView.setImageResource(R.drawable.d);

        }else{
            String str = getFileExtension(position);

            if(str.equalsIgnoreCase("jpg") || str.equalsIgnoreCase("jpeg") || str.equalsIgnoreCase("png")){
                File f = new File(objects.get(position).getPath());
                Uri imageUri = Uri.fromFile(f);

                RequestOptions cropOptions = new RequestOptions().centerCrop(context);
                Glide.with(context).load(imageUri).apply(cropOptions).into(imgView);
            }else
               imgView.setImageResource(R.drawable.a);
        }

        if(objects.get(position).isVisible()){
            cbox.setVisibility(View.VISIBLE);
        }



        return view;
    }

    private String getFileExtension(int position) {
        String path = objects.get(position).getPath();
        File file = new File(path);
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}

package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInListIcons;

@SuppressLint("ResourceAsColor")
public class CustomListMenuAccount extends ArrayAdapter<DataInListIcons> {
    private Activity Activity;
    private ArrayList<DataInListIcons> Data;
    private DisplayImageOptions options;
    SharedPreferences Login;

    public CustomListMenuAccount(Activity context, ArrayList<DataInListIcons> data) {
        super(context, R.layout.drawer_list_item, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def).showImageOnFail(R.mipmap.def)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView;

        rowView = inflater.inflate(R.layout.drawer_list_item, null, true);


        if (position >= 0) {
            TextView txtTitle = rowView.findViewById(R.id.Menutitle);
            ImageView imageView = rowView.findViewById(R.id.Menuicon);
            txtTitle.setText(Data.get(position).name);
            ImageLoader.getInstance().displayImage(Data.get(position).photo, imageView, options);
        }
        if (position == 4) {
            TextView txtTitle = rowView.findViewById(R.id.Menutitle);
            txtTitle.setTextColor(Color.parseColor("#DB3022"));
        }

        Animation animation = null;
        int mode = 0;

        switch (mode) {
            case 1:
                animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0, (float) 1.0);
                break;

            case 2:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.fade_in);
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.hyperspace_in);
                break;
            case 4:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.hyperspace_out);
                break;
            case 5:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.wave_scale);
                break;
            case 6:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_left_in);
                break;
            case 7:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_left_out);
                break;
            case 8:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_up_in);
                break;
            case 9:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_up_out);
                break;
            case 10:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.shake);
                break;
            case 11:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.slide_in_top);
                break;
            case 12:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.slide_top_to_bottom);
                break;
            case 13:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.slide_bottom_to_top);
                break;
        }
   //     animation.setDuration(500);

  //      rowView.startAnimation(animation);

        //   animation = null;

        return rowView;
    }


}

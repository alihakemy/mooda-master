package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInFilterData;
import dataInLists.DataInProduct;

@SuppressLint("ResourceAsColor")
public class CustomListMultiOptionsFilter extends ArrayAdapter<DataInFilterData.Sizes> {
    private Activity Activity;
    private ArrayList<DataInFilterData.Sizes> Data;
    private DisplayImageOptions options;

    public CustomListMultiOptionsFilter(Activity context, ArrayList<DataInFilterData.Sizes> data) {
        super(context, R.layout.singel_multi_option_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_multi_option_list, null, true);
        TextView txtName = rowView.findViewById(R.id.tv_name);
        LinearLayout List = rowView.findViewById(R.id.List);

        if (Data.get(position).size.isEmpty()) {
            Data.get(position).size = "0";
        }
        txtName.setText(Data.get(position).size.trim() + "");

        if (Data.get(position).selected) {
            Log.i("TestApp", "true " + position);
            List.setBackgroundResource(R.drawable.btn_white_red_radius0);
            txtName.setTextColor(Color.parseColor("#DE0000"));
        } else {
            Log.i("TestApp", "false " + position);
            List.setBackgroundResource(R.drawable.btn_white_radius0);
            txtName.setTextColor(Color.parseColor("#000000"));
        }


        Animation animation = null;
        int mode = 2;

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
        animation.setDuration(500);

        rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }

}

package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInPlans;

@SuppressLint("ResourceAsColor")
public class CustomListPlanDesc extends ArrayAdapter<DataInPlans.Details> {
    private Activity Activity;
    private ArrayList<DataInPlans.Details> Data;
    private byte Color;

    public CustomListPlanDesc(Activity context, ArrayList<DataInPlans.Details> data, byte Col) {
        super(context, R.layout.singel_plan_desc_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;
        this.Color = Col;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_plan_desc_list, null, true);
        TextView txtKey = rowView.findViewById(R.id.tv_Desc);

        txtKey.setText(Data.get(position).title + "");

        if (Color == 1) {
            txtKey.setTextColor(android.graphics.Color.WHITE);
        } else {
            txtKey.setTextColor(android.graphics.Color.BLACK);
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
        // animation.setDuration(500);
//
        //   rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}

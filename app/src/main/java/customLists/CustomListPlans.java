package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInPlans;

@SuppressLint("ResourceAsColor")
public class CustomListPlans extends ArrayAdapter<DataInPlans.Plans> {
    private Activity Activity;
    private int ID;
    private boolean Ckeck;
    private ArrayList<DataInPlans.Plans> Data;

    public CustomListPlans(Activity context, ArrayList<DataInPlans.Plans> data) {
        super(context, R.layout.singel_plan_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_plan_list, null, true);
        TextView tv_Title = rowView.findViewById(R.id.tv_name);
        TextView tv_Price = rowView.findViewById(R.id.tv_Price);
        LinearLayout List = rowView.findViewById(R.id.List);
        ExpandableHeightListView lvMain = rowView.findViewById(R.id.lvMain);


        tv_Title.setText(Data.get(position).title);
        tv_Price.setText(Data.get(position).price + " " + Activity.getResources().getString(R.string.DK3));

        lvMain.setExpanded(true);

        CustomListPlanDesc Adp ;
        if (Data.get(position).selected) {
            List.setBackgroundResource(R.drawable.btn_red_radius5);
            tv_Title.setTextColor(Color.WHITE);
            tv_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.checked4, 0);
            tv_Price.setTextColor(Color.WHITE);
            Adp = new CustomListPlanDesc(Activity, Data.get(position).details , (byte) 1);
        } else {
            List.setBackgroundResource(R.drawable.btn_white_red_radius5);
            tv_Title.setTextColor(Color.BLACK);
            tv_Title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.checked3, 0);
            tv_Price.setTextColor(Color.BLACK);
            Adp = new CustomListPlanDesc(Activity, Data.get(position).details , (byte) 2);
        }
        lvMain.setAdapter(Adp);


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

        //rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }
}

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

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInOrders;

@SuppressLint("ResourceAsColor")
public class CustomListOrdersMain extends ArrayAdapter<DataInOrders.MainData> {
    private Activity Activity;
    private ArrayList<DataInOrders.MainData> Data;

    public CustomListOrdersMain(Activity context, ArrayList<DataInOrders.MainData> data) {
        super(context, R.layout.singel_order_main_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_order_main_list, null, true);
        TextView tv_MainDate = rowView.findViewById(R.id.tv_MainDate);


        ExpandableHeightListView lvCart = rowView.findViewById(R.id.lvItems);


        CustomListOrders adapterCarts = new CustomListOrders(Activity, new ArrayList<>());
        lvCart.setExpanded(true);
        lvCart.setAdapter(adapterCarts);


        tv_MainDate.setText(Data.get(position).day + "");



        adapterCarts.addAll(Data.get(position).orders);
        adapterCarts.notifyDataSetChanged();


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
     //   animation.setDuration(500);

       // rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }

}

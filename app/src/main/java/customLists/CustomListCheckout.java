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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.usmart.com.moda.R;
import java.util.ArrayList;
import dataInLists.DataInCartCheckout;

@SuppressLint("ResourceAsColor")
public class CustomListCheckout extends ArrayAdapter<DataInCartCheckout.Store> {
    private Activity Activity;
    private ArrayList<DataInCartCheckout.Store> Data;

    public CustomListCheckout(Activity context, ArrayList<DataInCartCheckout.Store> data) {
        super(context, R.layout.singel_checkout_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_checkout_list, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView tv_Time = rowView.findViewById(R.id.tv_Time);
        TextView tv_Cost = rowView.findViewById(R.id.tv_Price);

        ExpandableHeightListView lvCart = rowView.findViewById(R.id.lvItems);


        CustomListCartCheckout adapterCarts = new CustomListCartCheckout(Activity, new ArrayList<>());
        lvCart.setExpanded(true);
        lvCart.setAdapter(adapterCarts);


        txtTitle.setText(Data.get(position).store_name + "");
        tv_Time.setText(Activity.getResources().getString(R.string.EstimateTime) + " : " +
                Data.get(position).min_estimated_time_day + " " + Data.get(position).min_estimated_time_month + " - " +  Data.get(position).max_estimated_time_day + " " + Data.get(position).max_estimated_time_month);
        tv_Cost.setText(
                Data.get(position).total_cost + " " + Activity.getResources().getString(R.string.DK));


        adapterCarts.addAll(Data.get(position).products);
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

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

import dataInLists.DataInPackages;

@SuppressLint("ResourceAsColor")
public class CustomListPackage extends ArrayAdapter<DataInPackages.PackagesDetails> {
    private Activity Activity;
    private ArrayList<DataInPackages.PackagesDetails> Data;

    public CustomListPackage(Activity context, ArrayList<DataInPackages.PackagesDetails> data) {
        super(context, R.layout.singel_package_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_package_list, null, true);
        TextView tv_Title = rowView.findViewById(R.id.tv_Title);
        TextView tv_Desc = rowView.findViewById(R.id.tv_Desc);
        TextView tv_Price = rowView.findViewById(R.id.tv_Price);

        TextView tv_Buy = rowView.findViewById(R.id.tv_Buy);




        tv_Title.setText(Data.get(position).title + "");
        tv_Desc.setText(Data.get(position).desc + "");
        tv_Price.setText(Data.get(position).price + " " + Activity.getResources().getString(R.string.DK3));

        tv_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
      //  animation.setDuration(500);

        //rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }

}

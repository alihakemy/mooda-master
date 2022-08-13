package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.moda.Login;
import com.usmart.com.moda.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInCartCheckout;
import dataInLists.DataInGlobal;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("ResourceAsColor")
public class CustomListOrderDetails extends ArrayAdapter<DataInCartCheckout.Store> {
    private Activity Activity;
    private ArrayList<DataInCartCheckout.Store> Data;
    private DisplayImageOptions options;

    public CustomListOrderDetails(Activity context, ArrayList<DataInCartCheckout.Store> data) {
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
       // TextView tv_ShipNum = rowView.findViewById(R.id.tv_ShipNum);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView tv_Time = rowView.findViewById(R.id.tv_Time);
        TextView tv_Cost = rowView.findViewById(R.id.tv_Price);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);
        ExpandableHeightListView lvCart = rowView.findViewById(R.id.lvItems);


        CustomListCartCheckout adapterCarts = new CustomListCartCheckout(Activity, new ArrayList<>());
        lvCart.setExpanded(true);
        lvCart.setAdapter(adapterCarts);


       /* tv_ShipNum.setText(Activity.getResources().getString(R.string.ShipmentNum) + " ( " +
                Data.get(position).shipment_number + " )");
        txtTitle.setText(Data.get(position).store.name + "");
        tv_Time.setText(Activity.getResources().getString(R.string.ShippingTime) + " : " +
                Data.get(position).store.estimated_arrival_time + "");
        tv_Cost.setText(Activity.getResources().getString(R.string.ShippingCost) + " " +
                Data.get(position).store.delivery_cost + " " + Activity.getResources().getString(R.string.DK) + " - " +
                Activity.getResources().getString(R.string.Total2) + " " + Data.get(position).store.total_price);*/



        adapterCarts.addAll(Data.get(position).products);
        adapterCarts.notifyDataSetChanged();




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

package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

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
import dataInLists.DataInNoti;
import dataInLists.DataInOrderItem;
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
public class CustomListCartOrders extends ArrayAdapter<DataInOrderItem.Products> {
    private Activity Activity;
    private ArrayList<DataInOrderItem.Products> Data;
    private DisplayImageOptions options;

    public CustomListCartOrders(Activity context, ArrayList<DataInOrderItem.Products> data) {
        super(context, R.layout.singel_cart_order_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_cart_order_list, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView tv_Type = rowView.findViewById(R.id.tv_Type);
        TextView txtMainPrice = rowView.findViewById(R.id.tv_MainPrice);
        TextView txtBeforePrice = rowView.findViewById(R.id.tv_BeforeDiscount);
        TextView txtAmount = rowView.findViewById(R.id.tv_amount);
        TextView tv_discount = rowView.findViewById(R.id.tv_discount);
        TextView tv_Status = rowView.findViewById(R.id.tv_Status);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);


        txtTitle.setText(Data.get(position).product_name + "");
        txtAmount.setText(Activity.getResources().getString(R.string.Amount) + " " + Data.get(position).count + "");
        tv_discount.setText(Data.get(position).offer_percentage + " % ");

        if (Data.get(position).type == 1) {
            tv_Type.setText(Activity.getResources().getString(R.string.Type1));
        } else {
            tv_Type.setText(Activity.getResources().getString(R.string.Type2));
        }
        if (Data.get(position).offer == 0) {
            txtMainPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            txtBeforePrice.setText("");
            txtBeforePrice.setVisibility(View.GONE);
            tv_discount.setVisibility(View.GONE);

        } else {
            txtBeforePrice.setPaintFlags(txtMainPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtBeforePrice.setVisibility(View.VISIBLE);
            txtMainPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            txtBeforePrice.setText(Data.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));
            txtBeforePrice.setVisibility(View.VISIBLE);
            tv_discount.setVisibility(View.VISIBLE);
        }

        if (Data.get(position).status == 1) {
            tv_Status.setText(R.string.OrderStatus1);
        } else if (Data.get(position).status == 2) {
            tv_Status.setTextColor(Color.parseColor("#068D0F"));
            tv_Status.setText(R.string.OrderStatus2);
        } else if (Data.get(position).status == 3) {
            tv_Status.setTextColor(Color.parseColor("##DB3022"));
            tv_Status.setText(R.string.OrderStatus3);
        } else if (Data.get(position).status == 4) {
            tv_Status.setTextColor(Color.parseColor("##DB3022"));
            tv_Status.setText(R.string.OrderStatus4);
        }


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, imageView, options);



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

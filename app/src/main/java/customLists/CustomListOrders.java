package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.moda.OrderDetails;
import com.usmart.com.moda.R;

import java.net.URL;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInOrders;

@SuppressLint("ResourceAsColor")
public class CustomListOrders extends ArrayAdapter<DataInOrders.Orders> {
    private Activity Activity;
    private ArrayList<DataInOrders.Orders> Data;
    private DisplayImageOptions options;

    public CustomListOrders(Activity context, ArrayList<DataInOrders.Orders> data) {
        super(context, R.layout.singel_order_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_order_list, null, true);
        TextView tv_Order = rowView.findViewById(R.id.tv_Order);
        TextView tv_Total = rowView.findViewById(R.id.tv_Total);
        TextView tv_Time = rowView.findViewById(R.id.tv_Time);
        TextView tv_Count = rowView.findViewById(R.id.tv_Count);
        TextView tv_Details = rowView.findViewById(R.id.tv_Details);
        ImageView multiImageView = rowView.findViewById(R.id.iv);
        LinearLayout List = rowView.findViewById(R.id.List);


        tv_Order.setText(Activity.getResources().getString(R.string.OrderIDMain) + " : "
                + Data.get(position).main_order_number);

        tv_Count.setText(Activity.getResources().getString(R.string.ProductsAmount) + " : "
                + Data.get(position).count);

        tv_Total.setText(Activity.getResources().getString(R.string.ProductsAmount) + " : "
                + Data.get(position).total_price);

        tv_Time.setText(Data.get(position).date + "");


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        if (!Data.get(position).images.isEmpty())
            ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).images.get(0), multiImageView, options);

    /*    for (int i = 0; i < Data.get(position).images.size(); i++) {
            try {
                multiImageView.putImages(Values.Link_Image + Data.get(position).images.get(i));
            } catch (Exception e) {
            }
        }
        if (Data.get(position).images.size() < 4) {
            for (int i = 0; i < 4 - (Data.get(position).images.size()); i++) {
                Uri uri = Uri.parse("android.resource://com.usmart.com.moda/mipmap/def2");
                multiImageView.putImages(uri.getPath());
            }
        }*/

        List.setOnClickListener(v -> {
            Intent i = new Intent(Activity, OrderDetails.class);
            i.putExtra("ID", Data.get(position).id);
            Activity.startActivity(i);
        });

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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch (Exception e) {
            // Log exception
            //  Log.i("TestApp_Bitmap", e.getMessage());
            return null;
        }
    }

}

package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.usmart.com.moda.R;

import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInAgents;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ResourceAsColor")
public class CustomListAgents extends ArrayAdapter<DataInAgents.Agents> {
    private Activity Activity;
    private ArrayList<DataInAgents.Agents> Data;
    private DisplayImageOptions options;

    public CustomListAgents(Activity context, ArrayList<DataInAgents.Agents> data) {
        super(context, R.layout.singel_agent_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_agent_list, null, true);
        TextView txtName = rowView.findViewById(R.id.tv_Name);
        LinearLayout btn_WhatsApp = rowView.findViewById(R.id.btn_WhatsApp);
        ImageView btn_Call = rowView.findViewById(R.id.btn_Call);
        CircleImageView imageView = rowView.findViewById(R.id.iv_Feeds);


        txtName.setText(Data.get(position).name + " ");

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
             //   .displayer(new RoundedBitmapDisplayer(10, 1))
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, imageView, options);

        btn_WhatsApp.setOnClickListener(v -> {
            PackageManager packageManager = Activity.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + Data.get(position).watsapp ;
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    Activity.startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        btn_Call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Data.get(position).phone));
            Activity.startActivity(callIntent);
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

        //  rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }


}

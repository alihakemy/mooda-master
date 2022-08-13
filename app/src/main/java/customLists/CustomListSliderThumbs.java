package customLists;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import constants.Values;
import dataInLists.ImageString;


public class CustomListSliderThumbs extends RecyclerView.Adapter<CustomListSliderThumbs.ViewHolder> {
    private Activity Activity;
    private ArrayList<ImageString> Data;
    private DisplayImageOptions options;
    //   private SliderLayout mDemoSlider;

    public CustomListSliderThumbs(Activity context, ArrayList<ImageString> data) {
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;
        //  this.mDemoSlider = Slider;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singel_slider_thumb, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image_path, holder.imageView, options);

        if (Data.get(position).selected) {
            holder.List.setBackground(Activity.getResources().getDrawable(R.drawable.btn_white_red_radius5));
        } else {
            holder.List.setBackground(Activity.getResources().getDrawable(R.drawable.btn_wight_gray_radius4));
        }

        if(Data.get(position).type == 1){
            holder.VidIcon.setVisibility(View.VISIBLE);
        }else{
            holder.VidIcon.setVisibility(View.GONE);
        }

       /* holder.List.setOnClickListener(v -> {
            //mDemoSlider.setCurrentPosition(position);
         /*   Intent i = new Intent(Activity, Cat_Products.class);
            i.putExtra("ID", Data.get(position).id);
            Activity.startActivity(i);* /
           // loaVideo(Data.get(position).link);

        });*/

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView , VidIcon;
        FrameLayout List;

        public ViewHolder(View rowView) {
            super(rowView);

            // get the reference of item view's
            imageView = rowView.findViewById(R.id.iv_Feeds);
            VidIcon   = rowView.findViewById(R.id.VidIcon);
            List = rowView.findViewById(R.id.List);
        }
    }

}

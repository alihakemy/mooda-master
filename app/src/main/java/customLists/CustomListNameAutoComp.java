package customLists;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.moda.Product;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInSearch;
public class CustomListNameAutoComp extends ArrayAdapter<DataInSearch.ProductDetails> {
    private ArrayList<DataInSearch.ProductDetails> items;
    private ArrayList<DataInSearch.ProductDetails> itemsAll;
    private ArrayList<DataInSearch.ProductDetails> suggestions;
    private Activity Activity;

    public CustomListNameAutoComp(Activity context, ArrayList<DataInSearch.ProductDetails> items) {
        super(context, R.layout.singel_product_list_search, items);
        this.items = items;
        this.itemsAll = (ArrayList<DataInSearch.ProductDetails>) items.clone();
        this.suggestions = new ArrayList<>();
        this.Activity = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.singel_product_list_search, null);
        }
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView txtPrice = rowView.findViewById(R.id.tv_MainPrice);
        TextView tv_BeforeDiscount = rowView.findViewById(R.id.tv_BeforeDiscount);
        TextView txtDiscountPersent = rowView.findViewById(R.id.tv_discount);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);
        FrameLayout List = rowView.findViewById(R.id.List);



        int customer = items.get(position).id ;
        if (customer != 0) {


            txtPrice.setText(items.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));

            if (items.get(position).offer == 0) {
                tv_BeforeDiscount.setVisibility(View.INVISIBLE);
                txtPrice.setText(items.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
                tv_BeforeDiscount.setText("");
                txtDiscountPersent.setVisibility(View.GONE);

            } else {
                tv_BeforeDiscount.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tv_BeforeDiscount.setVisibility(View.VISIBLE);
                txtPrice.setText(items.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
                tv_BeforeDiscount.setText(items.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));
                txtDiscountPersent.setVisibility(View.VISIBLE);

                float d = items.get(position).offer_percentage;
                if ((d - (int) d) != 0)
                    txtDiscountPersent.setText(Activity.getResources().getString(R.string.Discount) + "  " + items.get(position).offer_percentage + " %");
                else
                    txtDiscountPersent.setText(Activity.getResources().getString(R.string.Discount) + "  " + (int) Math.round(items.get(position).offer_percentage) + " %");
            }


          //  txtDiscount.setText(items.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));


            int x = items.get(position).title.length();
            if (x >= 70) {
                txtTitle.setText(Html.fromHtml(items.get(position).title.substring(0, 70)) + " ...");
            } else {
                txtTitle.setText(Html.fromHtml(items.get(position).title));
            }

            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.def)
                    .showImageForEmptyUri(R.mipmap.def)
                    .showImageOnFail(R.mipmap.def)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Values.Link_Image + items.get(position).image, imageView, options);

            List.setOnClickListener(v -> {
                Intent i = new Intent(Activity, Product.class);
                i.putExtra("ID", items.get(position).id);
                Activity.startActivity(i);
                //    Activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

            });
        }else {
            txtTitle.setText(Html.fromHtml(items.get(position).title));
            imageView.setVisibility(View.GONE);
            txtPrice.setVisibility(View.GONE);

        }
        return rowView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            DataInSearch.ProductDetails str = ((DataInSearch.ProductDetails) (resultValue));
            return str.title ;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DataInSearch.ProductDetails item : itemsAll) {
                    if (item.title.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DataInSearch.ProductDetails> filteredList = (ArrayList<DataInSearch.ProductDetails>) results.values;
            try {
                if (results != null && results.count > 0) {
                    clear();
                    for (DataInSearch.ProductDetails c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            } catch (Exception e) {

            }
        }
    };

}
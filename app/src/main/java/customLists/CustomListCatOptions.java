package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInCatOptions;

@SuppressLint("ResourceAsColor")
public class CustomListCatOptions extends ArrayAdapter<DataInCatOptions.OptionsValues> {
    private Activity Activity;
    private ArrayList<DataInCatOptions.OptionsValues> Data;
    private DisplayImageOptions options;

    public CustomListCatOptions(Activity context, ArrayList<DataInCatOptions.OptionsValues> data) {
        super(context, R.layout.singel_cats_child, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_cats_child, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        ImageView ImageView = rowView.findViewById(R.id.iv_Feeds);

        txtTitle.setText(Data.get(position).value);

        ImageView.setVisibility(View.GONE);

        return rowView;
    }
}

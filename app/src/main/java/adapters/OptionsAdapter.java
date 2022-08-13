package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usmart.com.moda.R;

import java.util.ArrayList;

import dataInLists.DataInCatOptions;


public class OptionsAdapter extends BaseAdapter {

    public static final String TAG = OptionsAdapter.class.getSimpleName();
    private ArrayList<DataInCatOptions.OptionsValues> valuesArrayList;
    private LayoutInflater inflater;

    public OptionsAdapter(Context ctx, ArrayList<DataInCatOptions.OptionsValues> valuesArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.valuesArrayList = valuesArrayList;
    }

    @Override
    public int getCount() {
        return valuesArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.singel_option_list, null);
        TextView tv_text1 = view.findViewById(R.id.tv_Key);

        tv_text1.setText(valuesArrayList.get(i).value+"");
     //   tv_address_type.setText(valuesArrayList.get(i).getValue());
        return view;
    }
}

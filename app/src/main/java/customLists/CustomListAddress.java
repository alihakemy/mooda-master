package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.usmart.com.moda.Checkout;
import com.usmart.com.moda.Login;
import com.usmart.com.moda.R;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInAddress;
import dataInLists.DataInGlobal;
import helpers.AddressHolder;
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
public class CustomListAddress extends ArrayAdapter<DataInAddress.Address> {
    private Activity Activity;
    private ArrayList<DataInAddress.Address> Data;
    private DisplayImageOptions options;
    private Typeface fontMedim, fontLight, fontUltra, fontAvenir, fontPoppinsMed;

    public CustomListAddress(Activity context, ArrayList<DataInAddress.Address> data) {
        super(context, R.layout.singel_address_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_address_list, null, true);
        TextView tv_Title = rowView.findViewById(R.id.tv_Title);
        TextView tv_Address = rowView.findViewById(R.id.tv_Address);
        TextView tv_City = rowView.findViewById(R.id.tv_City);
        TextView tv_DefAddress = rowView.findViewById(R.id.tv_DefAddress);
       // TextView Cancel = rowView.findViewById(R.id.Cancel);
        TextView tv_Type_Work = rowView.findViewById(R.id.tv_Type_Work);
        TextView tv_Type_Home = rowView.findViewById(R.id.tv_Type_Home);
        ImageView chk_address = rowView.findViewById(R.id.chk_address);



        tv_Title.setText(Data.get(position).title);
        tv_Address.setText(Data.get(position).street + " , " + Data.get(position).building + " , " + Data.get(position).gaddah);
        tv_City.setText(Data.get(position).area_name + "");

        if (Data.get(position).is_default) {
            AddressHolder.getInstance().setData(Data.get(position).id);
             tv_DefAddress.setVisibility(View.VISIBLE);
            chk_address.setImageResource(R.mipmap.checked4);
            // Cancel.setVisibility(View.GONE);
        } else {
             tv_DefAddress.setVisibility(View.GONE);
            chk_address.setImageResource(R.mipmap.checked3);
            // Cancel.setVisibility(View.VISIBLE);
        }

        if (Data.get(position).address_type == 1) {
            tv_Type_Home.setVisibility(View.VISIBLE);
            tv_Type_Work.setVisibility(View.GONE);
        } else {
            tv_Type_Work.setVisibility(View.VISIBLE);
            tv_Type_Home.setVisibility(View.GONE);
        }
     /*   tv_DefAddress.setOnClickListener(v -> {
            setDefAddress(Data.get(position).id);
            Intent i = new Intent(Activity, Checkout.class);
            i.putExtra("AddressID", Data.get(position).id);
            Activity.startActivity(i);
        });*/

       /* Cancel.setOnClickListener(v -> {
            loadWantDelete(position);
        });*/


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

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Data = (ArrayList<DataInAddress.Address>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInAddress.Address> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInAddress.Address> getFilteredResults(CharSequence constraint) {

        int x = Data.size();
        int y = 0;
        for (int i = 0; i < x; i++) {
            if (Data.get(y).title.contains(constraint) == true) {

            } else {
                Data.remove(y);
                y--;

            }
            y++;

        }
        return Data;
    }

    private void setDefAddress(int Address) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/setdefault/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + Address + "\"")
                .append("}").toString();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        String Auth;
        if (LoginHolder.getInstance().getData().equals("login")) {
            Auth = UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token;
        } else {
            Auth = Values.Authorization_User;
        }
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Auth)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    DataInGlobal Res;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Res = g.fromJson(response.body().string(), t);


                    Activity.runOnUiThread(() -> {
                        if (!Res.success) {
                            if (Res.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Res.message);
                            }
                        } else {


                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveAddress(int Pos) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + Data.get(Pos).id + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                        + " " + UserHolder.getInstance().getData().token.access_token)
                .delete(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInAddress Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInAddress>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    Activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Result.message);
                            }
                        } else {
                            remove(Data.get(Pos));
                            notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadWantDelete(int POS) {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.Yes);
        Text.setText(R.string.WantDelete);
        //   No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            RemoveAddress(POS);
            dialog.dismiss();
            Activity.finish();
            Activity.overridePendingTransition(0, 0);
            Activity.startActivity(Activity.getIntent());
            Activity.overridePendingTransition(0, 0);
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void loadMsg(String MSG) {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        Text.setText(MSG + "");
        No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

}

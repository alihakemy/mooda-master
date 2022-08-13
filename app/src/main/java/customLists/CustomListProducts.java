package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.moda.Login;
import com.usmart.com.moda.Product;
import com.usmart.com.moda.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInCatsLevels;
import dataInLists.DataInGlobal;
import dataInLists.DataInProducts;
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
public class CustomListProducts extends ArrayAdapter<DataInProducts.ProductDetails> {
    private Activity Activity;
    private ArrayList<DataInProducts.ProductDetails> Data;
    private DisplayImageOptions options;

    public CustomListProducts(Activity context, ArrayList<DataInProducts.ProductDetails> data) {
        super(context, R.layout.singel_product_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_product_list, null, true);

        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView txtPrice = rowView.findViewById(R.id.tv_MainPrice);
        TextView tv_Type = rowView.findViewById(R.id.tv_Type);
        TextView tv_BeforeDiscount = rowView.findViewById(R.id.tv_BeforeDiscount);
        TextView txtDiscountPersent = rowView.findViewById(R.id.tv_discount2);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);
        ImageView Fav = rowView.findViewById(R.id.Fav);
        LinearLayout List = rowView.findViewById(R.id.List);


        //   tv_Views.setText(Data.get(position).views + " " + Activity.getResources().getString(R.string.View));
        //   tv_Date.setText(Data.get(position).time + " ");
        txtPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
        if (Data.get(position).type == 1) {
            tv_Type.setText(Activity.getResources().getString(R.string.Type1));
        } else {
            tv_Type.setText(Activity.getResources().getString(R.string.Type2));
        }

        if (Data.get(position).offer == 0) {
            tv_BeforeDiscount.setVisibility(View.INVISIBLE);
            txtPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            tv_BeforeDiscount.setText("");
            txtDiscountPersent.setVisibility(View.GONE);

        } else {
            tv_BeforeDiscount.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_BeforeDiscount.setVisibility(View.VISIBLE);
            txtPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            tv_BeforeDiscount.setText(Data.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));
            txtDiscountPersent.setVisibility(View.VISIBLE);

            float d = Data.get(position).offer_percentage;
            if ((d - (int) d) != 0)
                txtDiscountPersent.setText(Activity.getResources().getString(R.string.Discount) + "  " + Data.get(position).offer_percentage + " %");
            else
                txtDiscountPersent.setText(Activity.getResources().getString(R.string.Discount) + "  " + (int) Math.round(Data.get(position).offer_percentage) + " %");
        }


        int x = Data.get(position).title.length();
        if (x >= 70) {
            txtTitle.setText(Html.fromHtml(Data.get(position).title.substring(0, 70)) + " ...");
        } else {
            txtTitle.setText(Html.fromHtml(Data.get(position).title));
        }
       /* if (Data.get(position).pin == 1) {
            txtPin.setVisibility(View.VISIBLE);
        } else {
            txtPin.setVisibility(View.GONE);
        }*/
        if (Data.get(position).favorite == true) {
            Fav.setImageResource(R.mipmap.like);
        } else {
            Fav.setImageResource(R.mipmap.non_like);
        }
        Fav.setOnClickListener(v -> {
            if (Data.get(position).favorite) {
                RemoveFave(position, Fav);
            } else {
                AddFave(position, Fav);
            }
        });


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_ImageProducts + Data.get(position).image, imageView, options);

       /* tv_More.setOnClickListener(v -> {
            Intent i = new Intent(Activity, Product.class);
            i.putExtra("ID", Data.get(position).id);
            Activity.startActivity(i);
        });*/


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
        // animation.setDuration(500);

        //  rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Data = (ArrayList<DataInProducts.ProductDetails>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInProducts.ProductDetails> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInProducts.ProductDetails> getFilteredResults(CharSequence constraint) {

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


    private void AddFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(Activity.getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorite/create/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"product_id\":\"" + Data.get(ID).id + "\"")
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
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    String Res = response.body().string();
                    Log.i("TestApp_5", Res);
                    Result = g.fromJson(Res, t);


                    Activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Result.message);
                            }
                        } else {
                            Data.get(ID).favorite = true;
                            LikeIc.setImageResource(R.mipmap.like);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(Activity.getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorite/destroy/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"product_id\":\"" + Data.get(ID).id + "\"")
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
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    String Res = response.body().string();
                    Log.i("TestApp_5", Res);
                    Result = g.fromJson(Res, t);


                    Activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Result.message);
                            }
                        } else {
                            Data.get(ID).favorite = false;
                            LikeIc.setImageResource(R.mipmap.non_like);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadMsgLogin(String MSG) {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        No.setText(R.string.Login);
        Text.setText(MSG + "");
        // No.setVisibility(View.GONE);


        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();

            Intent i = new Intent(Activity, Login.class);
            Activity.startActivity(i);

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

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

}

package customLists;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import dataInLists.DataInGlobal;
import dataInLists.DataInOffers;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.UserHolder;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

public class CustomListOffers2 extends RecyclerView.Adapter<CustomListOffers2.ViewHolder> {
    private Activity Activity;
    private ArrayList<DataInOffers.Ads> Data;
    private DisplayImageOptions options;

    public CustomListOffers2(Activity context, ArrayList<DataInOffers.Ads> data) {
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singel_product_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.txtPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
        holder.txtDiscount.setText(Data.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));
        holder.tv_Cat.setText(" ");

        if (Data.get(position).offer == 0) {
            //   txtDiscount.setVisibility(View.GONE);
            holder.txtPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            holder.txtDiscount.setText("");
            holder.txtDiscountPersent.setVisibility(View.GONE);

        } else {
            holder.txtDiscount.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDiscount.setVisibility(View.VISIBLE);
            holder.txtPrice.setText(Data.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            holder.txtDiscountPersent.setVisibility(View.VISIBLE);

            float d = Data.get(position).offer_percentage;
            if ((d - (int) d) != 0)
                holder.txtDiscountPersent.setText(" - " + Data.get(position).offer_percentage + " %");
            else
                holder.txtDiscountPersent.setText(" - " + (int) Math.round(Data.get(position).offer_percentage) + " %");
        }

        int x = Data.get(position).title.length();
        if (x >= 70) {
            holder.txtTitle.setText(Html.fromHtml(Data.get(position).title.substring(0, 70)) + " ...");
        } else {
            holder.txtTitle.setText(Html.fromHtml(Data.get(position).title));
        }

        if (Data.get(position).favorite == true) {
            holder.img_Fav.setImageResource(R.mipmap.like);
        } else {
            holder.img_Fav.setImageResource(R.mipmap.heart);
        }
        holder.img_Fav.setOnClickListener(view -> {
            if (Data.get(position).favorite) {
                RemoveFave(Data.get(position).id, holder.img_Fav);
            } else {
                AddFave(Data.get(position).id, holder.img_Fav);
            }
        });

        holder.img_Cart.setOnClickListener(view -> {
            AddCart(Data.get(position).id);

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
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, holder.imageView, options);

        holder.List.setOnClickListener(v -> {
            Intent i = new Intent(Activity, Product.class);
            i.putExtra("ID", Data.get(position).id);
            Activity.startActivity(i);
            //    Activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

        });

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtPrice, txtDiscount, txtDiscountPersent, tv_Cat ;
        ImageView imageView, img_Fav ,img_Cart ;
        FrameLayout List;

        public ViewHolder(View rowView) {
            super(rowView);

            // get the reference of item view's
            txtTitle = rowView.findViewById(R.id.tv_Title);
            txtPrice = rowView.findViewById(R.id.tv_MainPrice);
            txtDiscount = rowView.findViewById(R.id.tv_BeforeDiscount);
            txtDiscountPersent = rowView.findViewById(R.id.tv_discount);
            imageView = rowView.findViewById(R.id.iv_Feeds);
            img_Fav = rowView.findViewById(R.id.img_Fav);
          //  img_Cart = rowView.findViewById(R.id.img_Cart);
            tv_Cat = rowView.findViewById(R.id.tv_Cat);

            List = rowView.findViewById(R.id.List);

        }
    }

    private void AddFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(Activity.getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorites/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"product_id\":\"" + ID + "\"")
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
        String Url = Values.Link_service + "favorites/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"product_id\":\"" + ID + "\"")
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
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
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
                            LikeIc.setImageResource(R.mipmap.heart);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void AddCart(int ID) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/add/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(Activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Values.Authorization_User)
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
                            //CartNum.setText(Result.data.count + "");
                            loadMsg(Activity.getResources().getString(R.string.AddedToCart));
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

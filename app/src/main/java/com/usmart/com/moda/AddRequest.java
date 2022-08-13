package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInRequest;
import helpers.FaceIdHolder;
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


public class AddRequest extends FragmentActivity {
    Activity activity = AddRequest.this;
    SharedPreferences Login;
    EditText ed_Name, ed_Mobile, ed_Store, ed_ID, ed_Instagram, ed_Bank, ed_Iban,
            edit_Info , edit_Email;
    TextView tv_Name, tv_Store, tv_ID, tv_Instagram, tv_Bank, tv_Iban, tv_IDCard, edit_BackID,
            edit_FrontID, tv_Info, tv_Mobile, SetAddress , tv_Email;

    DataInRequest _Data = new DataInRequest();
    Button btn_Add;
    TextView MainTitle;
    ImageView HideAll;
    ProgressBar prog;
    String lang;
    String FrontImg, BackImg;

    ArrayList<String> uploadedFront = new ArrayList<>();
    ArrayList<String> uploadedAvatar = new ArrayList<>();
    private static final int PICK_IMAGE = 1001;
    private static final int PICK_IMAGE2 = 1002;


    private static final int INITIAL_REQUEST = 1337;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 1;
    private static final int READ_REQUEST = INITIAL_REQUEST + 2;
    private static final int WRITE_REQUEST = INITIAL_REQUEST + 3;
    private static final String[] CAMERA_PERMS = {
            android.Manifest.permission.CAMERA
    };
    private static final String[] READ_PERMS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] WRITE_PERMS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ImageView Test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        Resources activityRes = getResources();
        Configuration activityConf = activityRes.getConfiguration();
        Locale newLocale = new Locale("en");
        activityConf.setLocale(newLocale);
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = getApplicationContext().getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(newLocale);
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#23293F"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        //*******************************************************
        setContentView(R.layout.activity_add_request);
        // ***********************************************
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }


        Test = findViewById(R.id.Test);
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        ed_Name = findViewById(R.id.edit_Name);
        ed_Store = findViewById(R.id.edit_Store);
        ed_ID = findViewById(R.id.edit_ID);
        ed_Instagram = findViewById(R.id.edit_Instagram);
        ed_Bank = findViewById(R.id.edit_Bank);
        ed_Iban = findViewById(R.id.edit_Iban);
        edit_Info = findViewById(R.id.edit_Info);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        edit_Email = findViewById(R.id.edit_Email);
        btn_Add = findViewById(R.id.btn_Add);


        tv_Name = findViewById(R.id.tv_Name);
        tv_Store = findViewById(R.id.tv_Store);
        tv_Instagram = findViewById(R.id.tv_Instagram);
        tv_Bank = findViewById(R.id.tv_Bank);
        tv_Iban = findViewById(R.id.tv_Iban);
        tv_IDCard = findViewById(R.id.tv_IDCard);
        edit_BackID = findViewById(R.id.edit_BackID);
        edit_FrontID = findViewById(R.id.edit_FrontID);
        tv_Info = findViewById(R.id.tv_Info);
        tv_Mobile = findViewById(R.id.tv_Mobile);
        tv_Email = findViewById(R.id.tv_Email);
        tv_ID = findViewById(R.id.tv_ID);
        SetAddress = findViewById(R.id.SetAddress);

        MainTitle.setText(R.string.JoinRequestTitle);


        edit_FrontID.setOnClickListener(v -> {

            Options options = Options.init()
                    .setRequestCode(PICK_IMAGE)                                           //Request code for activity results
                    .setCount(1)                                                   //Number of images to restict selection count
                    .setPreSelectedUrls(uploadedFront)                               //Pre selected Image Urls
                    .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                    .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                    .setPath("/pix/images");                                       //Custom Path For media Storage

            Pix.start(AddRequest.this, options);
        });

        edit_BackID.setOnClickListener(v -> {

            Options options = Options.init()
                    .setRequestCode(PICK_IMAGE2)                                           //Request code for activity results
                    .setCount(1)                                                   //Number of images to restict selection count
                    .setPreSelectedUrls(uploadedAvatar)                               //Pre selected Image Urls
                    .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                    .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                    .setPath("/pix/images");                                       //Custom Path For media Storage

            Pix.start(AddRequest.this, options);
        });


        btn_Add.setOnClickListener(v -> {
            if (ed_Name.getText().toString().matches("")
                    || ed_Store.getText().toString().matches("") || ed_ID.getText().toString().matches("")
                    || ed_Bank.getText().toString().matches("") || ed_Iban.getText().toString().matches("")
                    || ed_Mobile.getText().toString().matches("")|| edit_Email.getText().toString().matches("") || ed_Instagram.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField), false);
                return;
            }
            if (Patterns.PHONE.matcher(ed_Mobile.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidMobile), false);
                return;
            }

            if (Patterns.EMAIL_ADDRESS.matcher(edit_Email.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidEmail), false);
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);
            btn_Add.setEnabled(false);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "join/request/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"name\":\"" + ed_Name.getText().toString() + "\",")
                    .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\",")
                    .append("\"email\":\"" + edit_Email.getText().toString() + "\",")
                    .append("\"shop\":\"" + ed_Store.getText().toString() + "\",")
                    .append("\"instagram\":\"" + ed_Instagram.getText().toString() + "\",")
                    .append("\"bank_name\":\"" + ed_Bank.getText().toString() + "\",")
                    .append("\"account_number\":\"" + ed_Iban.getText().toString() + "\",")
                    .append("\"id_number\":\"" + ed_ID.getText().toString() + "\",")
                    .append("\"details\":\"" + edit_Info.getText().toString() + "\",")
                    .append("\"front_image\":\"" + FrontImg + "\",")
                    .append("\"back_image\":\"" + BackImg + "\"")

                    .append("}").toString();

            Log.i("TestApp_5", UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token);
            Log.i("TestApp_5", json);

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
                    //
                    try {
                        Gson g = new Gson();
                        Type t = new TypeToken<DataInRequest>() {
                        }.getType();
                        String XX = response.body().string();
                        Log.i("TestApp_3", XX);
                        _Data = g.fromJson(XX, t);


                        activity.runOnUiThread(() -> {
                            if (!_Data.success) {
                                loadMsg(_Data.message, false);
                            } else {
                                loadMsg(getResources().getString(R.string.RequestAdded), true);
                            }
                        });

                    } catch (Exception e) {
                        Log.i("TestApp_3_Error", e.getMessage() + "");
                    }
                }

            });

            btn_Add.setEnabled(true);
            HideAll.setVisibility(View.GONE);
            prog.setVisibility(View.GONE);
        });
    }


    public Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 2400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeFile(filePath, o2);

    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null && resultCode != RESULT_CANCELED) {
            ArrayList<String> selectedImages = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (selectedImages.size() > 0) {
                for (String im : selectedImages) {
                    if (!im.isEmpty()) {
                        edit_FrontID.setText(im);
                        FrontImg = UploadPhotos(im, (byte) 1);
                        Log.i("TestApp_3_Error", im + "");
                    }
                }
            }
        }

        if (requestCode == PICK_IMAGE2 && data != null && resultCode != RESULT_CANCELED) {
            ArrayList<String> selectedImages = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (selectedImages.size() > 0) {
                for (String im : selectedImages) {
                    if (!im.isEmpty()) {
                        edit_BackID.setText(im);
                        BackImg = UploadPhotos(im, (byte) 2);
                        Log.i("TestApp_3_Error", im + "");
                    }
                }
            }
        }


    }

    public String UploadPhotos(final String uploaded, byte i) {
        final String[] XYZ = new String[1];
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                Bitmap resized, bitmap, bitmap2;

                bitmap2 = decodeFile(uploaded);
                bitmap = imageOreintationValidator(bitmap2, uploaded);
                resized = Bitmap.createScaledBitmap(bitmap2, (int) (bitmap2.getWidth() * 0.75),
                        (int) (bitmap.getHeight() * 0.75), true);
                String X = UploadImage(bitmap2, i);
                Log.i("TestApp_22", bitmap2.toString() + "");
                XYZ[0] = X;
                msg = X;

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                XYZ[0] = msg;
            }
        }.execute(null, null, null);
        return XYZ[0];
    }


    public String UploadImage(Bitmap bit, byte i) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 90, stream); // compress to
        // which format
        // you want.
        byte[] byte_arr = stream.toByteArray();
        String YY = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        YY = YY.replace(" ", "");
        if (i == 1) {
            FrontImg = "data:image/jpeg;base64," + YY.replace(" ", "").replace("\n", "");
        }
        if (i == 2) {
            BackImg = "data:image/jpeg;base64," + YY.replace(" ", "").replace("\n", "");
        }
      //  byte[] pdfAsBytes = Base64.decode(YY, 0);

       /* File filePath = new File(Environment.getExternalStorageDirectory() + "/braodcasts");
        FileOutputStream os = null;*/
        try {
             writeFileOnInternalStorage(activity, "front.txt", FrontImg);
             writeFileOnInternalStorage(activity, "back.txt", BackImg);
        } catch (Exception e) {
            Log.i("TestApp_2233_w", e.getMessage() + "");
        }

        Log.i("TestApp_2233_er", FrontImg + "");
        return YY;
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/braodcast");
       // File dir = new File(mcoContext.getFilesDir(), "braodcast");
        if (!dir.exists()) {
            Log.i("TestApp_2233_errror",  "dir.exists");
            dir.mkdir();
        }
        if (!dir.exists()) {
            Log.i("TestApp_2233_errror",  "dir.exists22");
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TestApp_2233_errror", e.getMessage() + "");
        }
    }

    private void loadMsg(String MSG, boolean t) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        Text.setText(MSG + "");
        No.setVisibility(View.GONE);


        Yes.setOnClickListener(v -> {
            if (t) {
                startActivity(new Intent(activity, Home.class));
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void setOnBack() {
        super.onBackPressed();
    }

    private void loadMsgDone(String MSG) {
        final Dialog dialog = new Dialog(activity);
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
            startActivity(new Intent(activity, Cart.class));
        });
        dialog.show();
    }

    private boolean canAccessCamera() {
        return (hasPermission(android.Manifest.permission.CAMERA));
    }

    private boolean canReadStorage() {
        return (hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private boolean canWriteStorage() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return false;
    }

    @Override
    protected void onResume() {

        if (!canAccessCamera()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(CAMERA_PERMS, CAMERA_REQUEST);
            }
        }
        if (!canReadStorage()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(READ_PERMS, READ_REQUEST);
            }
        }
        if (!canWriteStorage()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(WRITE_PERMS, WRITE_REQUEST);
            }
        }
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "login");
                editor.putString("UserID", FaceIdHolder.getInstance().getData());
                editor.putString("User", UserHolder.getInstance().getData().toString());
                editor.commit();

            } else {
                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                LoginHolder.getInstance().setData("logout");
                FaceIdHolder.getInstance().setData("0");
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "logout");
                editor.putString("UserID", "0");
                editor.putString("User", "");
                editor.commit();

                startActivity(new Intent(activity, Login.class));
            }
        } catch (Exception e) {

        }
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        setOnBack();
    }

    public void gotoBack(View v) {
        setOnBack();
    }
}


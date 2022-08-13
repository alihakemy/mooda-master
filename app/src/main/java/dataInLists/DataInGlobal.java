package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInGlobal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInGlobal() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;
    public Data data = new Data();

    public class Data {
        public int id;
        public int count;
        public int visitor_id;
        public int product_id;
        public String my_balance;
        public String phone;
        public String instegram;
        public String watsapp;
        public String delivery_cost;
        public float min_free_delivery;
        public boolean show_ad;
        public boolean status;
        public String created_at;
        public String updated_at;
        public String instagram;
        public String twitter;
        public String snap_chat;
        public String facebook;
        public String app_phone;
        public String email;

    }
}

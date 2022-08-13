package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInOrderItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInOrderItem() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public MainData data = new MainData();

    public class MainData {
        public Order order  ;
        public Address address  ;
        public ArrayList<Store> stores = new ArrayList<>();
    }

    public class Order {
        public int id;
        public String main_order_number;
        public String date;
        public byte payment_method;
        public float subtotal_price;
        public float delivery_cost;
        public float total_price;
    }


    public class Store {
        public int id;
        public int store_id;
        public int main_id;
        public byte status;
        public String store_name;
        public String date;
        public String from_delivery_date;
        public String to_delivery_date;
        public String store_logo;
        public String order_number;
        public String subtotal_price;
        public String delivery_cost;
        public String total_price;
        public ArrayList<Products> products = new ArrayList<>();

    }
    public class Products {
        public int id;
        public int type;
        public byte offer;
        public int count;
        public int item_id;
        public byte status;
        public String product_name;
        public String image;
        public String store_name;
        public float offer_percentage;
        public String price_before_offer;
        public float final_price;
        public boolean show_refund_button;
    }
    public class Address {
        public int id;
        public int user_id;
        public double latitude ;
        public double longitude ;
        public String title;
        public byte address_type;
        public int area_id;
        public String area;
        public String gaddah;
        public String building;
        public String floor;
        public String apartment_number;
        public String street;
        public String extra_details;
        public String phone;
        public String piece;
        public boolean is_default;
        public String created_at;
        public String updated_at;
    }


}

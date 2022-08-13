package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInProduct implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProduct() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;

    public Data data = new Data();

    public class Data {
        public Product product;
        public int views;
        public Store store = new Store();
        public ArrayList<Related> related;
        public ArrayList<Related> user_other_ads;
        public ArrayList<Features> features;

    }

    public class Store {
        public int id;
        public String cover;
        public String logo;
        public String name;
    }

    public class Product {
        public int id;
        public int count;
        public int store_id;
        public int option_id;
        public int remaining_quantity;
        public String title;
        public String option_name;
        public String option_value;
        public String description;
        public String video;
        public String video_image;
        public byte type;
        public byte offer;
        public byte multi_options;
        public ArrayList<String> images;
        public String final_price;
        public String price_before_offer;
        public float offer_percentage;
        public int category_id;
        public String  image;
        public String  category_name;
        public boolean favorite;
        public ArrayList<Properties> properties;
        public ArrayList<ProductImages> product_images;
        public MultiOptions multiple_option;
    }


    public class User implements Serializable {
        public int id;
        public String image;
        public String name;
        public String phone;
        public String watsapp;
        public String email;
    }

    public class Features implements Serializable {
        public String image;
        public String title;
    }
    public class ProductImages implements Serializable {
        public String image;
        public String link;
    }

    public class Related {
        public int id;
        public String title;
        public String image;
        public String final_price;
        public String price_before_offer;
        public byte offer;
        public float offer_percentage;
        public String weight;
        public String kg;
        public String numbers;
        public int category_id;
        public boolean favorite;
        public String  category_name;
    }

    public class MultiOptions implements Serializable {
        public int option_value_id;
        public String option_name;
        public ArrayList<OptionsValues> option_values;
    }
    public class OptionsValues implements Serializable {
        public int option_value_id;
        public int remaining_quantity;
        public String value;
        public boolean selected = false;
        public OptionsData option_data;
    }

    public class OptionsData implements Serializable {
        public int option_id;
        public int option_value_id;
        public int total_quatity;
        public int remaining_quantity;
        public String final_price;
        public String price_before_offer;
        public String barcode;
        public String stored_number;
    }

    public class Properties implements Serializable {
        public String category_name;
        public ArrayList<PropertiesOptions> options;
    }
    public class PropertiesOptions implements Serializable {
        public String value;
        public String key;
    }

}

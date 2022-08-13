package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInProducts implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProducts() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public MainData data;


    public class MainData {
        public ArrayList<Ads> slider;
        public Store store = new Store();
        public ArrayList<SubCats> categories = new ArrayList<>();
        public Products products;
    }

    public class SubCats {
        public int id;
        public String title;
        public String image;
        public boolean selected;
    }

    public class Products {
        public int current_page;
        public ArrayList<ProductDetails> data = new ArrayList<>();
        public String first_page_url;
        public String from;
        public String next_page_url;
        public String path;
        public String prev_page_url;
        public String to;
        public int per_page;

    }

    public class ProductDetails {
        public int id;
        public String title;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public byte type;
        public float offer_percentage;
        public int category_id;
        public int remaining_quantity;
        public String category_name;
        public String image;
        public String numbers;
        public String weight;
        public boolean favorite;
    }

    public class Store {
        public int id;
        public String cover;
        public String logo;
        public String name;
    }

    public class Ads implements Serializable {
        public int id;
        public byte type;
        public byte content_type;
        public byte ad_type;
        public String image;
        public String content;
        public String logo;
        public String name;
        public int store_id;
    }
}

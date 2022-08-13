package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInProductsOnly implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInProductsOnly() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public Products data;

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
        public float price;
        public float final_price;
        public float price_before_offer;
        public byte offer;
        public float offer_percentage;
        public int category_id;
        public String category_name;
        public String image;
        public String numbers;
        public String weight;
        public boolean favorite;
    }
}

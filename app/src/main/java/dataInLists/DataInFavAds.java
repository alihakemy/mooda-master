package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInFavAds implements Serializable {


    private static final long serialVersionUID = 1L;

    public DataInFavAds() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public ArrayList<Product> data = new ArrayList<>();

    public class Product {
        public int id;
        public String image;
        public String title;
        public String price;
        public String final_price;
        public String price_before_offer;
        public String category_name;
        public int offer;
        public int category_id;
        public float offer_percentage;
        public boolean favorite;
        public int type;
    }

}

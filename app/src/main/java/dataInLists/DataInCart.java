package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInCart implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInCart() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public MainData data = new MainData();

    public class MainData {
        public float subtotal_price;
        public int count  ;
        public ArrayList<Carts> cart = new ArrayList<>();
    }

    public class Carts {
        public int id;
        public int count;
        public int type;
        public int store_id;
        public int offer;
        public int option_id;
        public String option_name ;
        public String size_name ;
        public String size_value ;
        public String option_value ;
        public String title;
        public String store_name;
        public String image;
        public String final_price;
        public float offer_percentage;
        public String price_before_offer;
        public boolean favorite;
        public boolean edit = false;
    }
}

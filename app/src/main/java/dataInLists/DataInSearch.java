package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInSearch implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInSearch() {
        super();
    }


    public boolean success;
    public int code;
    public ArrayList<ProductDetails> data = new ArrayList<>();

    public class ProductDetails {
        public int id;
        public String title;
        public String final_price;
        public String price;
        public String price_before_offer;
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

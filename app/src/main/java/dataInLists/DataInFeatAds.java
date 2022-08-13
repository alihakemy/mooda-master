package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInFeatAds implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInFeatAds() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public  Ads data = new Ads();

    public class Ads {
        public int id;
        public ArrayList<Product> data = new ArrayList<>();
    }

    public class Product {
        public int id;
        public String image;
        public String title;
        public String price;
        public String description;
        public boolean favorite;
    }

}

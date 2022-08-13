package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInAds implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInAds() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public  Data data = new Data();

    public class Data {
        public  ArrayList<Ads> ended_ads = new ArrayList<>();
        public  ArrayList<Ads> current_ads = new ArrayList<>();
    }
    public class Ads {
        public int id;
        public int views;
        public byte pin;
        public String main_image;
        public String title;
        public String price;
        public String publication_date;
    }

}

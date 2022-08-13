package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataInOffers implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInOffers() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public Offers data;

    public class Offers {
        public ArrayList<OfferContent> offers;
        public ArrayList<Slider> slider;

    }
    public class OfferContent {
        public byte type = 1 ;
        public String icon;
        public String title;
        public ArrayList<Ads> ads;
    }

    public class Ads {
        public int id;
        public int place;
        public byte type;
        public byte offer;
        public byte multi_options;
        public float offer_percentage;
        public float final_price;
        public float price_before_offer;
        public boolean favorite;
        public String image;
        public String title;
        public String content;
        public String content_type;
        public String created_at;
        public String updated_at;
    }

    public class Slider implements Serializable {
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

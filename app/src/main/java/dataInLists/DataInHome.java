package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInHome implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInHome() {
        super();
    }


    public boolean success;
    public boolean next_page;
    public int code;
    public String message;
    public HomeData data;

    public class HomeData {
        public ArrayList<Ads> slider;
        public ArrayList<Ads> content;
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

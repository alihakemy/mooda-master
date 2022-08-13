package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInAdEditItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2L;

    public DataInAdEditItem() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public Main data = new Main();

    public class Main implements Serializable {
        public Ad ad = new Ad();
        public String category_names;
        public ArrayList<Images> ad_images = new ArrayList();
        public ArrayList<Features> features = new ArrayList();
    }

    public class Ad implements Serializable {
        public int id;
        public int user_id;
        public int category_name;
        public int category_id;
        public int sub_category_id;
        public int sub_category_two_id;
        public int sub_category_three_id;
        public int sub_category_four_id;
        public int sub_category_five_id;
        public String title;
        public String price;
        public String description;
        public String main_image;
        public String created_at;
        public String updated_at;
    }

    public class Images implements Serializable {
        public int id;
        public int product_id;
        public String image;
    }

    public class Features implements Serializable {
        public int id;
        public int product_id;
        public String target_id;
        public String value;
        public int option_id;
        public String type;
    }
}

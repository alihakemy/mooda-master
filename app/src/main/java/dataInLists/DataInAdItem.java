package dataInLists;

import java.io.Serializable;

public class DataInAdItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInAdItem() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public Ad data = new Ad();

    public class Ad {
        public int id;
        public int user_id;
        public int category_id;
        public int sub_category_id;
        public int sub_category_two_id;
        public int sub_category_three_id;
        public int sub_category_four_id;
        public int sub_category_five_id;
        public String title;
        public String price;
        public String description;
        public String created_at;
        public String updated_at;

    }
}





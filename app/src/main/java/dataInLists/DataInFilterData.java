package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInFilterData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInFilterData() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;
    public Data data = new Data();

    public class Data {
        public float min_price;
        public float max_price;
        public ArrayList<Cats> categories;
        public ArrayList<Sizes> sizes;
        public ArrayList<Types> type;
    }

    public class Cats {
        public int id;
        public String title;
    }

    public class Sizes {
        public int id;
        public String size;
        public boolean selected;
    }
    public class Types {
        public int id;
        public String size;
    }
}

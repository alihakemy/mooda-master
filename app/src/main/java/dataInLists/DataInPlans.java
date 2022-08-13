package dataInLists;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataInPlans implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInPlans() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public Main data = new Main();

    public class Main {
        public ArrayList<Plans> plans = new ArrayList<>();
    }

    public class Plans {
        public int id;
        public String title;
        public String title_en;
        public String cat_id;
        public String price;
        public boolean selected = false ;
        public ArrayList<Details> details = new ArrayList<>();
    }

    public class Details {
        public int id;
        public String title;
        public String title_en;
        public int plan_id;
        public String status;
    }
}

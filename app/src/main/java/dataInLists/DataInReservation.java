package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInReservation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInReservation() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;
    public Data androidData = new Data();
    public Data data = new Data();

    public class Data implements Serializable {
        public int id;
        public String main_order_number;
        public String date;
        public String time;
        public String url;
        public String image;
        public int count;
        public float total_cost;

    }
}

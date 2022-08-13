package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInReservation2 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInReservation2() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;

    public Data androidData = new Data();

    public class Data implements Serializable {
        public int id;
        public String title;
        public String date;
        public String url;
        public String image;
        public int count;
        public String total_cost;

    }
}

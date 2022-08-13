package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInOrders implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInOrders() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public ArrayList<MainData> data = new ArrayList<>();

    public class MainData {
        public String day;
        public ArrayList<Orders> orders = new ArrayList<>();
    }

    public class Orders {
        public int id;
        public String main_order_number;
        public ArrayList<String> images;
        public String date;
        public String total_price;
        public int count;
    }
}

package dataInLists;

import java.io.Serializable;

public class DataInCoupon implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInCoupon() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;
    public Data data = new Data();

    public class Data {
        public float discount_percentage;
        public String total_without_discount;
        public String total_without_d;
        public String total_with_discount;
        public String total_w_discount;
        public String discount_value;
        public String discount_v;
        public String discount_code;

    }
}

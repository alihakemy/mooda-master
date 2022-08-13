package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInEmptyArray implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInEmptyArray() {
        super();
    }


    public boolean success;
    public int code;
    public String message_en;
    public String message_ar;
    public String message;
    public ArrayList<Data> data = new ArrayList<>();

    public class Data {
        public int id;
    }
}

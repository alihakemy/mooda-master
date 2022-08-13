package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInPackages implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInPackages() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public String message_en;
    public String message_ar;
    public Packages data = new Packages();

    public class Packages {
        public ArrayList<PackagesDetails> packages = new ArrayList<>();
    }

    public class PackagesDetails {
        public int id;
        public String title;
        public String price;
        public String desc;
        public int amount;

    }
}

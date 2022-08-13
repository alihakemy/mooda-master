package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInAllCats implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInAllCats() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public Main data = new Main();


    public class Main {
        public ArrayList<Cats> categories = new ArrayList<>();
        public Store store = new Store();
    }

    public class Cats {
        public int id;
        public String image;
        public String title;
        public int products_count;
    }


    public class Store {
        public int id;
        public String cover;
        public String logo;
        public String name;
    }
}

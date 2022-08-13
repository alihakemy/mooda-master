package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInAgents implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInAgents() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public Main data ;


    public class Main {
        public  ArrayList<Agents> elmndobeen = new ArrayList<>();
    }

    public class Agents {
        public int id;
        public String image;
        public String name;
        public String phone;
        public String watsapp;
    }

}

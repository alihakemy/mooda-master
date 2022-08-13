package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataInCatOptions implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInCatOptions() {
        super();
    }


    public boolean success;
    public int code;
    public String message;
    public MainData data;

    public class MainData {
        public ArrayList<Options> options;
    }

    public class Options {
        public int option_id;
        public String is_required;
        public String type;
        public String title;
        public ArrayList<OptionsValues> values = new ArrayList<>();
    }

    public class OptionsValues {
        public int value_id;
        public String value;
    }

}

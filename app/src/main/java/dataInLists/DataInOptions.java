package dataInLists;

public class DataInOptions {

    public DataInOptions() {
        super();
    }

    public DataInOptions(int op_id, String op_title, boolean req) {
        option_id = op_id;
        option_value = "";
        option_value_id = 0;
        option_title = op_title;
        required = req;
    }

    public DataInOptions(int op_id, String op_title, String op_val, boolean req) {
        option_id = op_id;
        option_value = op_val;
        option_title = op_title;
        option_value_id = 0;
        required = req;
    }

    public DataInOptions(int op_id, String op_title, int op_val_id, String op_val, boolean req) {
        option_id = op_id;
        option_value = op_val;
        option_value_id = op_val_id;
        option_title = op_title;
        required = req;
    }

    public int option_id;
    public String option_title;
    public String option_value = "";
    public int option_value_id = 0;
    public boolean required;

}

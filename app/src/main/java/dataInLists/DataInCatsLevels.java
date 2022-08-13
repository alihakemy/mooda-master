package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInCatsLevels implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInCatsLevels() {
        super();
    }


    public boolean success;
    public int code;
    public MainData data = new MainData();

    public class MainData {
        public Category category = new Category();
        public ArrayList<Category> sub_categories = new ArrayList<>();
        public ArrayList<Category> sub_category_array = new ArrayList<>();
        public ArrayList<Category> categories = new ArrayList<>();
        public ArrayList<Ad> ad_image = new ArrayList<>();
        public Category sub_category_level1 = new Category();
        public Category sub_category_level2 = new Category();
        public Category sub_category_level3 = new Category();
        public Category sub_category_level4 = new Category();
        public Category sub_category_level5 = new Category();
        public Products products;
    }

    public class Category {
        public Category(int id) {
            this.id = id;
        }
        public Category(int id , String title) {
            this.id = id;
            this.title = title;
        }

        public Category() {
        }

        public int id;
        public int category_id;
        public int sub_category_id;
        public String title;
        public String image;
        public boolean selected = false;
        public boolean next_level = false;
    }

    public class Products {
        public int current_page;
        public ArrayList<ProductDetails> data = new ArrayList<>();
        public String first_page_url;
        public String from;
        public String next_page_url;
        public String path;
        public String prev_page_url;
        public String to;
        public int per_page;

    }

    public class ProductDetails {
        public int id;
        public int views;
        public String title;
        public String price;
        public byte pin;
        public boolean favorite;
        public String time;
        public String image;
        public String created_at;
    }

    public class Ad {
        public String image;
    }

}

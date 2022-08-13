package dataInLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DataInAdEditProduct implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataInAdEditProduct() {
        super();
    }

    public String category_names;
    public ArrayList<Images> ad_images = new ArrayList();
    public ArrayList<String> Upload_images = new ArrayList();
    public HashMap<Integer, DataInOptions> features = new HashMap<>();

    public int id;
    public int user_id;
    public int category_name;
    public ArrayList<Integer> categories;
    public int city_id;
    public int area_id;
    public byte share_location;
    public double lat;
    public double lng;
    public int plan_id;
    public int category_id;
    public int sub_category_id;
    public int sub_category_two_id;
    public int sub_category_three_id;
    public int sub_category_four_id;
    public int sub_category_five_id;
    public String title;
    public String price;
    public String description;
    public String Mobile;
    public String main_image;
    public String created_at;
    public String updated_at;

    public String getCategory_names() {
        return category_names;
    }

    public void setCategory_names(String category_names) {
        this.category_names = category_names;
    }

    public int getCategory_name() {
        return category_name;
    }

    public void setCategory_name(int category_name) {
        this.category_name = category_name;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Integer> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getUpload_images() {
        return Upload_images;
    }

    public void setUpload_images(ArrayList<String> upload_images) {
        Upload_images = upload_images;
    }

    public ArrayList<Images> getAd_images() {
        return ad_images;
    }

    public void setAd_images(ArrayList<Images> ad_images) {
        this.ad_images = ad_images;
    }

    public HashMap<Integer, DataInOptions> getFeatures() {
        return features;
    }

    public void setFeatures(HashMap<Integer, DataInOptions> features) {
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public int getSub_category_two_id() {
        return sub_category_two_id;
    }

    public void setSub_category_two_id(int sub_category_two_id) {
        this.sub_category_two_id = sub_category_two_id;
    }

    public int getSub_category_three_id() {
        return sub_category_three_id;
    }

    public void setSub_category_three_id(int sub_category_three_id) {
        this.sub_category_three_id = sub_category_three_id;
    }

    public int getSub_category_four_id() {
        return sub_category_four_id;
    }

    public void setSub_category_four_id(int sub_category_four_id) {
        this.sub_category_four_id = sub_category_four_id;
    }

    public int getSub_category_five_id() {
        return sub_category_five_id;
    }

    public void setSub_category_five_id(int sub_category_five_id) {
        this.sub_category_five_id = sub_category_five_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public byte getShare_location() {
        return share_location;
    }

    public void setShare_location(byte share_location) {
        this.share_location = share_location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public class Images {
        public int id;
        public int product_id;
        public String image;
    }

    public class Features {
        public int id;
        public int product_id;
        public String target_id;
        public String value;
        public int option_id;
        public String type;
    }

    @Override
    public String toString() {
        return "DataInAdEditProduct{" +
                "category_names='" + category_names + '\'' +
                ", features=" + features +
                ", id=" + id +
                ", plan_id=" + plan_id +
                ", user_id=" + user_id +
                ", category_name=" + category_name +
                ", categories=" + categories +
                ", city_id=" + city_id +
                ", area_id=" + area_id +
                ", share_location=" + share_location +
                ", lat=" + lat +
                ", lng=" + lng +
                ", category_id=" + category_id +
                ", sub_category_id=" + sub_category_id +
                ", sub_category_two_id=" + sub_category_two_id +
                ", sub_category_three_id=" + sub_category_three_id +
                ", sub_category_four_id=" + sub_category_four_id +
                ", sub_category_five_id=" + sub_category_five_id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", main_image='" + main_image + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", ad_images=" + ad_images +
                ", Upload_images=" + Upload_images +
                '}';
    }
}

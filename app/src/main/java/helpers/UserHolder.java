package helpers;

import dataInLists.DataInUser;

public class UserHolder {
    private DataInUser.Data FaceID;

    public DataInUser.Data getData() {
        return FaceID;
    }

    public void setData(DataInUser.Data data) {
        this.FaceID = data;
    }

    public void setData() {
        this.FaceID = new DataInUser().new Data();
    }

    private static final UserHolder holder = new UserHolder();

    public static UserHolder getInstance() {
        return holder;
    }

}

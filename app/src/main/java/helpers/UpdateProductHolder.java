package helpers;

import dataInLists.DataInAdEditProduct;

public class UpdateProductHolder {
    private DataInAdEditProduct FaceID;

    public DataInAdEditProduct getData() {
        return FaceID;
    }

    public void setData(DataInAdEditProduct data) {
        this.FaceID = data;
    }

    public void setData() {
        this.FaceID = new DataInAdEditProduct();
    }

    private static final UpdateProductHolder holder = new UpdateProductHolder();

    public static UpdateProductHolder getInstance() {
        return holder;
    }

}

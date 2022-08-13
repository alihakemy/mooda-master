package helpers;

import dataInLists.DataInAdEditProduct;

public class ProductDataHolder {
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

    private static final ProductDataHolder holder = new ProductDataHolder();

    public static ProductDataHolder getInstance() {
        return holder;
    }

}

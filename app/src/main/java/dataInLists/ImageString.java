package dataInLists;

import java.io.Serializable;

public class ImageString implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ImageString() {
        super();
    }

    public ImageString(String path, boolean sel) {
        image_path = path;
        selected = sel;
        type = 0;
    }

    public ImageString(String path, boolean sel, byte t) {
        image_path = path;
        selected = sel;
        type = t;
    }

    public String image_path;
    public boolean selected = false;
    public byte type;

}

package com.pinpinbox.android.pinpinbox2_0_0.bean;

import java.util.List;

/**
 * Created by vmage on 2017/11/21.
 */

public class ItemAlbumExplore {


    private String name;

    private String url;

    private List<ItemAlbum> itemAlbumList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemAlbum> getItemAlbumList() {
        return itemAlbumList;
    }

    public void setItemAlbumList(List<ItemAlbum> itemAlbumList) {
        this.itemAlbumList = itemAlbumList;
    }


}

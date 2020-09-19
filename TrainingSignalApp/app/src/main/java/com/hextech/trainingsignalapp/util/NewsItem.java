package com.hextech.trainingsignalapp.util;
import java.io.Serializable;

public class NewsItem implements Serializable {
    public String uuid, title, link, summary, publisher, author, type, offnet, ignore_main_image, published_at, is_magazine, reference_id;
    public NewsImageItem main_image;
}

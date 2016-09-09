package com.myworld.wenwo.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 16/8/29.
 */

public class Tag{
    public String type;
    public List<String> childs=new ArrayList<>();

    public Tag(String type) {
        this.type = type;
    }
}

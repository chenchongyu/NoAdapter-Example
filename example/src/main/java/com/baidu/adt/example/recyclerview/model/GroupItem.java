package com.baidu.adt.example.recyclerview.model;

/**
 * Author： chenchongyu
 * Date: 2019-07-19
 * Description:
 */
public class GroupItem {
    public String groupName;
    public String text;

    public boolean animateShown;

    public GroupItem(String groupName, String text) {
        this.groupName = groupName;
        this.text = text;
    }

    public GroupItem(String text) {
        this.text = text;
    }
}

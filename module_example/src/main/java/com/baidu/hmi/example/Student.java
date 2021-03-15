/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi.example;

public class Student {
    private String name;
    private int id;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

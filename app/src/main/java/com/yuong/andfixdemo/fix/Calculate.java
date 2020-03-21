package com.yuong.andfixdemo.fix;

import com.yuong.andfixdemo.Replace;

public class Calculate {
    @Replace(clazz = "com.yuong.andfixdemo.Calculate", method = "calculate")
    public int calculate() {
        int i = 10;
        int j = 1;
        return i / j;
    }

}

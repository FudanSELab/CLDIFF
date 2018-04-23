package edu.fdu.se.main.astdiff.RQ;

import java.io.File;

/**
 * Created by huangkaifeng on 2018/4/23.
 *
 */
public class Temp {


    public static void main(String args[]){
        Temp a = new Temp();
        a.a();
    }
    public void a(){
        Temp.B aaa = new Temp.B();
        System.out.println(aaa.getClass().getCanonicalName());
        System.out.println(aaa.getClass().getName());

        System.out.println(this.getClass().getCanonicalName());
        System.out.println(this.getClass().getName());

    }
    class B{

    }
}

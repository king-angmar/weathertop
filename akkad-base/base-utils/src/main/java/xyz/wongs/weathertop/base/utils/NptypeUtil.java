package xyz.wongs.weathertop.base.utils;

public class NptypeUtil {

    public static String[] getNpType(String npType) {
        //                携出-携入方   001-电信 002-移动 003-联通
        //String arr[] ={"001","002"};
        String carry_in = null;
        String carry_out = null;
        switch (npType) {
            case "102":
                carry_in = "002";
                carry_out = "001";
                break;
            case "103":
                carry_in = "003";
                carry_out = "001";
                break;
            case "201":
                carry_in = "001";
                carry_out = "002";
                break;
            case "203":
                carry_in = "003";
                carry_out = "002";
                break;
            case "301":
                carry_in = "001";
                carry_out = "003";
                break;
            case "302":
                carry_in = "001";
                carry_out = "003";
                break;
        }
        String arr[] = {carry_out, carry_in};
        return arr;
    }
}

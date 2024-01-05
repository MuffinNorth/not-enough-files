package ru.muffinnorth.nef.utils;

public class ColorUtils {

    public static int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }

    public static int[] getColorFromInt(int color) {
        var colorArray = new int[3];
        colorArray[0] = (color & 0x00FF0000) >> 16;
        colorArray[1] = (color & 0x0000FF00) >> 8;
        colorArray[2] = color & 0x000000FF;
        return colorArray;
    }
}

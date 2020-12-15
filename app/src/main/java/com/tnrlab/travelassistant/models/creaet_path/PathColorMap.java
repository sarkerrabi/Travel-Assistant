package com.tnrlab.travelassistant.models.creaet_path;

import com.mapbox.mapboxsdk.style.expressions.Expression;

public class PathColorMap {
    private int selectedColorView = 0;

    public PathColorMap(int selectedColorView) {
        this.selectedColorView = selectedColorView;
    }

    public Expression getColorByData(int value) {
        if (selectedColorView == 0) {
            return getColorValueBySpeedValue(value);
        } else {
            return null;
        }
    }

    private Expression getColorValueBySpeedValue(int speed) {
        if (speed == 0) {
            return Expression.rgb(255, 235, 238);
        } else if (speed == 1) {
//            return Color.parseColor("#ffcdd2");
            return Expression.rgb(255, 205, 210);

        } else if (speed == 2) {
//            return Color.parseColor("#ef9a9a");
            return Expression.rgb(239, 154, 154);

        } else if (speed == 3) {
//            return Color.parseColor("#e57373");
            return Expression.rgb(229, 115, 115);

        } else if (speed == 4) {
//            return Color.parseColor("#ef5350");
            return Expression.rgb(239, 83, 80);

        } else if (speed == 5) {
//            return Color.parseColor("#f44336");
            return Expression.rgb(244, 67, 54);

        } else if (speed == 6) {
//            return Color.parseColor("#e53935");
            return Expression.rgb(229, 57, 53);

        } else if (speed == 7) {
//            return Color.parseColor("#d32f2f");
            return Expression.rgb(211, 47, 47);

        } else if (speed == 8) {
//            return Color.parseColor("#c62828");
            return Expression.rgb(198, 40, 40);

        } else if (speed == 9) {
//            return Color.parseColor("#b71c1c");
            return Expression.rgb(183, 28, 28);

        } else if (speed == 10) {
            return Expression.rgb(150, 13, 13);

        } else if (speed == 11) {
//            return Color.parseColor("#690d0d");
            return Expression.rgb(105, 13, 13);

        } else {
            return Expression.rgb(105, 13, 13);

        }

    }


}

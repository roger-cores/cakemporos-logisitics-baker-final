package in.cakemporos.logistics.cakemporoslogistics.utilities;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderWeight;

/**
 * Created by roger on 11/7/2016.
 */
public class EnumFormatter {

    public static float getWeight(OrderWeight orderWeight){
        switch (orderWeight){

            case HALF:
                return 0.5f;
            case ONE:
                return 1f;
            case ONEANDHALF:
                return 1.5f;
            case TWO:
                return 2f;
            default:
                return 0;

        }
    }

}

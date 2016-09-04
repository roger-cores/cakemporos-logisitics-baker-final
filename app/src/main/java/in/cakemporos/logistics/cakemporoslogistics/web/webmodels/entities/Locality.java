package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import java.io.Serializable;

/**
 * Created by roger on 10/8/16.
 */
public class Locality extends EntityBase{

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

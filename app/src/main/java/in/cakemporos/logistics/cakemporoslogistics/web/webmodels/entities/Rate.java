package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

/**
 * Created by Maitreya on 03-Sep-16.
 */
public class Rate extends EntityBase {

    private String name;

    private Integer value;

    private Boolean flat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getFlat() {
        return flat;
    }

    public void setFlat(Boolean flat) {
        this.flat = flat;
    }
}

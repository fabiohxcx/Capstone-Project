package fabiohideki.com.megagenerator.model;

/**
 * Created by fabio.lagoa on 20/02/2018.
 */

public class LotteryPlace {

    private Geometry geometry;
    private String name;
    private String openNow;
    private String vicinity;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}

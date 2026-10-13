public class Plot {

    // && means AND
    // || means OR

    private String plotID;
    private String name;
    private String staticDwelling;
    private String dwelling;
    private boolean hasPower;
    private int bedrooms;
    private int size; //(in m2)
    private int cost;
    private String facilities;

    public Plot(String plotID, String name, String staticDwelling, String dwelling, boolean hasPower, int bedrooms, int size, int cost, String facilities) {
        setPlotID(plotID);
        setName(name);
        setStaticDwelling(staticDwelling);
        setDwelling(dwelling);
        setHasPower(hasPower);
        setBedrooms(bedrooms);
        setSize(size);
        setCost(cost);
        setFacilities(facilities);
    }

    public String getPlotID() {
        return plotID;
    }
    public void setPlotID(String plotID) {
        if (onlyContainsNumbers(plotID))
            this.plotID = plotID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaticDwelling() {
        return staticDwelling;
    }

    public void setStaticDwelling(String staticDwelling) {
        this.staticDwelling = staticDwelling;
    }

    public String getDwelling() {
        return dwelling;
    }

    public void setDwelling(String dwelling) {
        this.dwelling = dwelling;
    }

    public boolean isPowered() {
        return hasPower;
    }

    public void setHasPower(boolean hasPower) {
        if (isStaticDwelling()) {
            this.hasPower = true;
        } else {
            this.hasPower = hasPower;
        }
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    private boolean onlyContainsNumbers(String text) {
        return (text.matches("[0-9]+"));
    }

    public boolean isStaticDwelling() {
        return staticDwelling != null && isValidStaticDwelling(staticDwelling);
    }

    private boolean isValidStaticDwelling(String value) {
        return value.equalsIgnoreCase("Mobile Home")
                || value.equalsIgnoreCase("Bungalow")
                || value.equalsIgnoreCase("Apartment");
    }

    private boolean isValidDwelling(String value) {
        return value.equalsIgnoreCase("Empty Pitch")
                || value.equalsIgnoreCase("Tent")
                || value.equalsIgnoreCase("Caravan")
                || value.equalsIgnoreCase("Campervan")
                || value.equalsIgnoreCase("Motor home");
    }
}
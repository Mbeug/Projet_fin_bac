package com.group13.augmentedView.libs.places;

/**
 *
 * @author Maxime Beugoms
 * @author Florian Duprez
 * @author Baptiste Lapiere
 * @author Martin Meerts
 *
 * This class is the object to store buidling information
 */

public class PlaceInfo {

    private String name;
    private String icon;
    private String isOpened;
    private String rating;
    private String phone;
    private String website;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsOpened(String isOpened) {
        this.isOpened = isOpened;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
       this.icon = icon;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String[] toStringArray() {
        return new String[] {name, icon, isOpened, rating, phone, website};
    }

    @Override
    public String toString() {
        return name + "\n" + icon + "\n" + isOpened + "\n" +
                rating + "\n" + phone + "\n" + website + "\n";
    }
}

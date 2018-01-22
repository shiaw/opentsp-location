package com.navinfo.opentsp.platform.push;

import java.util.ArrayList;
import java.util.List;

/**
 * The variant of request user data which allow select from list of values. <p/>
 * Request can be displayed as group of radiobuttons or single combobox if multi=false, and as group of checkbox or
 * single list if it is multi.
 */
public class DataRequestList extends DataRequest {
    private boolean customValue;
    private List<Object> list;
    private boolean multi;

    /**
     * Allow custom values. It allow to choose value not only from list, but and allow user to add custom value into list.
     * @return
     */
    public boolean isCustomValue() {
        return customValue;
    }

    /**
     * Allow custom values. It allow to choose value not only from list, but and allow user to add custom value into list.
     * @param customValue
     */
    public void setCustomValue(boolean customValue) {
        this.customValue = customValue;
    }

    /**
     * List of values from which user can choose.
     * @return
     */
    public List<Object> getList() {
        return list;
    }

    /**
     * List of values from which user can choose.
     * @param list
     */
    public void setList(List<Object> list) {
        this.list = list;
    }

    /**
     * If this flag is enable then user can choose multiple values, also request will return List of values and
     * allow using of java.util.Collection in {@link #getValue()}.
     * @return
     */
    public boolean isMulti() {
        return multi;
    }

    /**
     * If this flag is enable then user can choose multiple values, also request will return List of values and
     * allow using of java.util.Collection in {@link #getValue()}.
     * @param multi
     */
    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    @Override
    public DataRequestList clone() {
        DataRequestList clone = (DataRequestList) super.clone();
        clone.list = new ArrayList<>(list);
        return clone;
    }
}

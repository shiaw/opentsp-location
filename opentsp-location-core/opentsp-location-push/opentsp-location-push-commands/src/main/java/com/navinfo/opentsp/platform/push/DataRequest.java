package com.navinfo.opentsp.platform.push;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * request for user data, usually it can be displayed as field in dialog but i unspecified. <p/>
 * <b>Format<b/>
 * At current status of spec the format is unspecified.
 */
public class DataRequest implements Cloneable {

    @NotNull
    private ValueType valueType;
    private String format;
    private Object min;
    private Object max;
    private Object value;
    private String id;
    private String title;
    private ComponentType type;
    private Map<String, Object> attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public <T> T getAttribute(String key, Class<T> type) {
        if(attributes == null) {
            return null;
        }
        Object o = attributes.get(key);
        return type.cast(o);
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Type value of this input.
     * @return
     */
    public ValueType getValueType() {
        return valueType;
    }

    /**
     * Type value of this input.
     * @param valueType
     */
    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    /**
     * Format string of this input, it value is not required and depends from type.
     * @return
     */
    public String getFormat() {
        return format;
    }

    /**
     * Format string of this input, it value is not required and depends from type.
     * @param format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Minimal value or null.
     * @return
     */
    public Object getMin() {
        return min;
    }

    /**
     * Minimal value or null.
     * @param min
     */
    public void setMin(Object min) {
        this.min = min;
    }

    /**
     * Maximal value or null.
     * @return
     */
    public Object getMax() {
        return max;
    }

    /**
     * Maximal value or null.
     * @param max
     */
    public void setMax(Object max) {
        this.max = max;
    }

    /**
     * Initial value or null
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * Initial value or null
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public DataRequest clone() {
        try {
            DataRequest  clone = (DataRequest) super.clone();
            clone.attributes = new HashMap<>(this.attributes);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

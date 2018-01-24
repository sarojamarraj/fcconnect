package com.freightcom.migration;

public class FieldSpecification
{
    private final String columnName;
    private final String type;
    private final String defaultValue;
    private final String key;
    private final String mapped;
    private final String new_type;
    private final String value_expression;
    private final String special;

    public FieldSpecification(final String[] fields)
    {
        this.columnName = fields[0];
        this.type = fields[1];
        this.defaultValue = fields[2];

        if (fields.length > 3 && ! fields[3].equals("")) {
            this.key = fields[3];
        } else {
            this.key = "";
        }

        if (fields.length > 4 && ! fields[4].equals("")) {
            this.mapped = fields[4];
        } else {
            this.mapped = this.columnName;
        }

        if (fields.length > 5 && ! fields[5].equals("")) {
            this.new_type = fields[5];
        } else {
            this.new_type = type;
        }

        if (fields.length > 6 && ! fields[6].equals("")) {
            this.value_expression = fields[6];
        } else {
            this.value_expression = null;
        }

        if (fields.length > 7 && ! fields[7].equals("")) {
            this.special = fields[7];
        } else {
            this.special = null;
        }
    }

    public String getColumnName()
    {
        return columnName;
    }

    public String getType()
    {
        return type;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public String getKey()
    {
        return key;
    }

    public String getMapped()
    {
        return mapped;
    }

    public String toString()
    {
        return "[F " + columnName + "->" + mapped + "]";
    }

    public String getNewType()
    {
        return new_type;
    }

    public String getValueExpression()
    {
        return value_expression;
    }

    public boolean hasValueExpression()
    {
        return value_expression != null;
    }

    public String getSpecial()
    {
        return special;
    }

    public boolean specialIs(String value) {
        return special != null && special.equalsIgnoreCase(value);
    }

}

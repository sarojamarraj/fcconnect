package com.freightcom.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TableMap {
	private final String tableName;
	private final String mappedTableName;
	private final Map<String,FieldSpecification> columns = new HashMap<String,FieldSpecification>();

	public TableMap(String tableName, String mappedTableName) {
		this.tableName = tableName;
		this.mappedTableName = mappedTableName;
	}

	public void add(String[] fields) {
		columns.put(fields[0],  new FieldSpecification(fields));
	}

    public void addNew(String[] fields) {
        FieldSpecification spec = new FieldSpecification(fields);
        columns.put(spec.getMapped(), spec);
    }

	public String getTableName() {
		return tableName;
	}
	
	public Set<String> getColumnNames() {
		return columns.keySet();
	}
	
	public FieldSpecification getColumnSpecification(String columnName) {
		return columns.get(columnName);
	}

	public String getMappedTableName() {
		return mappedTableName;
	}
	
	public String toString() {
		return "[tableMap " + tableName + "->" + mappedTableName + "]";
	}

    public Collection<FieldSpecification> getFields()
    {
        return columns.values();
    }
}

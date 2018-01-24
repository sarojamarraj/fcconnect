package com.freightcom.migration;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class KeyDropper {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final JdbcTemplate template;

	public KeyDropper(DataSource dataSource) {
		template = new JdbcTemplate(dataSource);
	}
	
	public StringBuilder getCommands() {
		final StringBuilder commands = new StringBuilder();
		final String query = "SELECT concat('alter table ',table_schema,'.',table_name,' DROP FOREIGN KEY ',constraint_name,';') as 'command' FROM information_schema.table_constraints WHERE constraint_type='FOREIGN KEY' and table_schema=(select database());";
		
		for (Map<String,Object> commandObject: template.queryForList(query)) {
			commands.append(commandObject.get("command")).append("\n");
		}
		
		log.info("\n\nDROP COMMANDS\n\n" + commands + "\n\n");
		
		return commands;
	}

}

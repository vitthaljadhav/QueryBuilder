package com.jcg.jdbc.sql.query.builder;

public interface DbProperties {

	// Database Configuration Properties
	public final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public final static String JDBC_DB_URL = "jdbc:mysql://localhost:3306/tutorialDb";

	// Database Connection Properties
	public final static String JDBC_USER = "root";
	public final static String JDBC_PASS = "root";

	// Database Table & Column Name Properties
	public final static String TABLE_NAME = "employee_table";
	public final static String COLUMN_ONE = "employee_id";
	public final static String COLUMN_TWO = "employee_name";
	public final static String COLUMN_THREE = "employee_salary";
}
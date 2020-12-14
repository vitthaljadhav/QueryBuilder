package com.jcg.jdbc.sql.query.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.BinaryCondition.Op;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.DropQuery;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.OrderObject.Dir;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

public class Querybuilder implements DbProperties {

	static ResultSet resObj;
	static Statement stmtObj;
	static Connection connObj;

	static DbSchema schemaObj;
	static DbSpec specficationObj;

	static DbTable table_name;
	static DbColumn column_1, column_2, column_3, column_4;

	public final static Logger logger = Logger.getLogger(Querybuilder.class);

	// Helper Method #1 :: This Method Is Used To Create A Connection With The
	// Database
	public static void connectDb() {
		try {
			Class.forName(JDBC_DRIVER);
			connObj = DriverManager.getConnection(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
			logger.info("\n=======Database Connection Open=======\n");

			stmtObj = connObj.createStatement();
			logger.info("\n=======Statement Object Created=======\n");

			loadSQLBuilderSchema();
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
	}

	// Helper Method #2 :: This Method Is Used To Create Or Load The Default Schema
	// For The SQLBuilder
	private static void loadSQLBuilderSchema() {
		specficationObj = new DbSpec();
		schemaObj = specficationObj.addDefaultSchema();
	}

	// Helper Method #3 :: This Method To Used To Close The Connection With The
	// Database
	public static void disconnectDb() {
		try {
			stmtObj.close();
			connObj.close();
			logger.info("\n=======Database Connection Closed=======\n");
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
	}

	// SQLQueryBuilder #1 :: This Method Is Used To Perform The Create Operation In
	// The Database
	public static void createDbTable() {
		logger.info("\n=======Creating '" + TABLE_NAME + "' In The Database=======\n");
		try {
			// Specifying Table Name
			table_name = schemaObj.addTable(TABLE_NAME);

			// Specifying Column Names For The Table
			column_1 = table_name.addColumn(COLUMN_ONE, Types.INTEGER, 10);
			column_2 = table_name.addColumn(COLUMN_TWO, Types.VARCHAR, 100);
			column_3 = table_name.addColumn(COLUMN_THREE, Types.INTEGER, 200);

			String createTableQuery = new CreateTableQuery(table_name, true).validate().toString();
			logger.info("\nGenerated Sql Query?= " + createTableQuery + "\n");
			stmtObj.execute(createTableQuery);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n=======The '" + TABLE_NAME + "' Successfully Created In The Database=======\n");
	}

	// SQLQueryBuilder #2 :: This Method Is Used To Perform The Insert Operation In
	// The Database
	public static void insertDataInTable(int id, String name, int salary) {
		String insertTableQuery;
		logger.info("\n=======Inserting Record In The '" + TABLE_NAME + "'=======\n");
		try {
			insertTableQuery = new InsertQuery(table_name).addColumn(column_1, id).addColumn(column_2, name)
					.addColumn(column_3, salary).validate().toString();
			logger.info("\nGenerated Sql Query?= " + insertTableQuery + "\n");
			stmtObj.execute(insertTableQuery);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n=======Record Sucessfully Inserted  In The '" + TABLE_NAME + "'=======\n");
	}

	// SQLQueryBuilder #3 :: This Method Is Used To Display All Records From The
	// Database
	public static void displayRecords() {
		String displayRecordsQuery;
		logger.info("\n=======Displaying All Records From The '" + TABLE_NAME + "'=======\n");
		try {
			displayRecordsQuery = new SelectQuery().addColumns(column_1).addColumns(column_2).addColumns(column_3)
					.validate().toString();
			logger.info("\nGenerated Sql Query?= " + displayRecordsQuery + "\n");

			resObj = stmtObj.executeQuery(displayRecordsQuery);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId?= " + resObj.getString(COLUMN_ONE) + ", Name?= " + resObj.getString(COLUMN_TWO)
							+ ", Salary?= " + resObj.getString(COLUMN_THREE) + "\n");
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
	}

	// SQLQueryBuilder #4 :: This Method Is Used To Display A Specific Record From
	// The Database
	public static void displaySelectiveRecord(int emp_id) {
		String selectiveRecordQuery;
		logger.info("\n=======Displaying Specific Record From The '" + TABLE_NAME + "'=======\n");
		try {
			selectiveRecordQuery = new SelectQuery().addColumns(column_1).addColumns(column_2).addColumns(column_3)
					.addCondition(BinaryCondition.equalTo(column_1, emp_id)).validate().toString();
			logger.info("\nGenerated Sql Query?= " + selectiveRecordQuery + "\n");

			resObj = stmtObj.executeQuery(selectiveRecordQuery);
			if (!resObj.next()) {
				logger.info("\n=======No Record Is Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId?= " + resObj.getString(COLUMN_ONE) + ", Name?= " + resObj.getString(COLUMN_TWO)
							+ ", Salary?= " + resObj.getString(COLUMN_THREE) + "\n");
				} while (resObj.next());
			}
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n=======Specific Record Displayed From The '" + TABLE_NAME + "'=======\n");
	}

	// SQLQueryBuilder #5 :: This Method Is Used To Update A Record In The Database
	public static void updateRecord(int update_record_id) {
		String updateRecord, editorName = "Java Code Geek";
		logger.info("\n=======Updating Record In The '" + TABLE_NAME + "'=======\n");
		try {
			updateRecord = new UpdateQuery(table_name).addSetClause(column_2, editorName)
					.addCondition(BinaryCondition.equalTo(column_1, update_record_id)).validate().toString();
			logger.info("\nGenerated Sql Query?= " + updateRecord + "\n");
			stmtObj.execute(updateRecord);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n=======Record Updated In The '" + TABLE_NAME + "' =======\n");
	}

	// SQLQueryBuilder #6 :: This Method Is Used To Delete A Specific Record From
	// The Table
	public static void deleteSelectiveRecord(int delete_record_id) {
		String deleteSelectiveRecordQuery;
		logger.info("\n=======Deleting Specific Record From The '" + TABLE_NAME + "'=======\n");
		try {
			deleteSelectiveRecordQuery = new DeleteQuery(table_name)
					.addCondition(BinaryCondition.equalTo(column_1, delete_record_id)).validate().toString();
			logger.info("\nGenerated Sql Query?= " + deleteSelectiveRecordQuery + "\n");
			stmtObj.execute(deleteSelectiveRecordQuery);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n=======Selective Specific Deleted From The '" + TABLE_NAME + "'=======\n");
	}

	// SQLQueryBuilder #7 :: This Method Is Used To Delete All Records From The
	// Table
	public static void deleteRecords() {
		String deleteRecordsQuery;
		logger.info("\n=======Deleting All Records From The '" + TABLE_NAME + "'=======\n");
		try {
			deleteRecordsQuery = new DeleteQuery(table_name).validate().toString();
			logger.info("\nGenerated Sql Query?= " + deleteRecordsQuery + "\n");
			stmtObj.execute(deleteRecordsQuery);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n=======All Records Deleted From The '" + TABLE_NAME + "'=======\n");
	}

	// SQLQueryBuilder #8 :: This Method Is Used To Drop A Table From The Database
	@SuppressWarnings("static-access")
	public static void dropTableFromDb() {
		String dropTableQuery;
		logger.info("\n=======Dropping '" + TABLE_NAME + "' From The Database=======\n");
		try {
			dropTableQuery = new DropQuery(DropQuery.Type.TABLE, table_name).dropTable(table_name).validate()
					.toString();
			logger.info("\nGenerated Sql Query?= " + dropTableQuery + "\n");
			stmtObj.execute(dropTableQuery);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		logger.info("\n======='" + TABLE_NAME + "' Is Dropped From The Database=======\n");
	}

	public static void getStudents() {
		SelectQuery sql = new SelectQuery();
		String selectQuery = sql.addAllColumns().addCustomFromTable("stdtbl").addCustomOrdering("fname", Dir.ASCENDING).validate().toString();
		
		logger.info("\nGenerated Sql Query?= " + selectQuery + "\n");
		try {

			resObj = stmtObj.executeQuery(selectQuery);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id") + ",First  Name= " + resObj.getString("fname")
							+ ", Last Name= " + resObj.getString("lname") + "\n");
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  
	public static void innerJoin() {
		try {
			SelectQuery sqlQuery = new SelectQuery();
			String innerJoin = sqlQuery.addAllColumns().addCustomJoin(SelectQuery.JoinType.INNER,"stdtbl","employee_table" ,BinaryCondition.equalTo("employee_table.employee_id", "stdtbl.id")).validate().toString();
			String innerJoinUpdated = innerJoin.replace("'", "");
			logger.info("\n Inner Join Generated Sql Query?= " + innerJoinUpdated + "\n");
			
			resObj = stmtObj.executeQuery(innerJoinUpdated);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id") + ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname") + "\n" +"Employee_ Id = " + resObj.getString(COLUMN_ONE) + ",Employee Name= " + resObj.getString(COLUMN_TWO)
							+ ", Salary= " + resObj.getString(COLUMN_THREE));
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void leftJoin() {
		try {
			SelectQuery sqlQuery = new SelectQuery();
			String leftjoin = sqlQuery.addAllColumns().addCustomJoin(SelectQuery.JoinType.LEFT_OUTER,"stdtbl","employee_table" ,BinaryCondition.equalTo("employee_table.employee_id", "stdtbl.id")).validate().toString();
			String leftJoinUpdated = leftjoin.replace("'", "");
			logger.info("\n Left Join Generated Sql Query?= " + leftJoinUpdated + "\n");
			
			resObj = stmtObj.executeQuery(leftJoinUpdated);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id") + ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname") + "\n" +"Employee_ Id = " + resObj.getString(COLUMN_ONE) + ",Employee Name= " + resObj.getString(COLUMN_TWO)
							+ ", Salary= " + resObj.getString(COLUMN_THREE));
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void rightJoin() {
		try {
			SelectQuery sqlQuery = new SelectQuery();
			String rightJoin = sqlQuery.addAllColumns().addCustomJoin(SelectQuery.JoinType.RIGHT_OUTER,"stdtbl","employee_table" ,BinaryCondition.equalTo("employee_table.employee_id", "stdtbl.id")).validate().toString();
			String rightJoinUpdated = rightJoin.replace("'", "");
			logger.info("\n Right Join Generated Sql Query?= " + rightJoinUpdated + "\n");
			
			resObj = stmtObj.executeQuery(rightJoinUpdated);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id") + ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname") + "\n" +"Employee_ Id = " + resObj.getString(COLUMN_ONE) + ",Employee Name= " + resObj.getString(COLUMN_TWO)
							+ ", Salary= " + resObj.getString(COLUMN_THREE));
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void fullJoin() {
		try {
			SelectQuery sqlQuery = new SelectQuery();
			String fullJoin = sqlQuery.addAllColumns().addCustomJoin(SelectQuery.JoinType.FULL_OUTER,"stdtbl","employee_table" ,BinaryCondition.equalTo("employee_table.employee_id", "stdtbl.id")).validate().toString();
			String fullJoinUpdated = fullJoin.replace("'", "");
			logger.info("\n full Join Generated Sql Query?= " + fullJoinUpdated + "\n");
			
			resObj = stmtObj.executeQuery(fullJoinUpdated);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id") + ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname") + "\n" +"Employee_ Id = " + resObj.getString(COLUMN_ONE) + ",Employee Name= " + resObj.getString(COLUMN_TWO)
							+ ", Salary= " + resObj.getString(COLUMN_THREE));
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void inOperator() {
	
		try {//selectQuery.addCondition(new InCondition(table.findColumn(col.name), ins));
			SelectQuery selectQuery = new SelectQuery();			
			//selectQuery.addCustomColumns(new CustomSql("id"));
			selectQuery.addAllColumns();
			selectQuery.addCustomFromTable(new CustomSql("stdtbl"));
			selectQuery.addCondition(new InCondition(new CustomSql("id"),101,109));
			String sql = selectQuery.validate().toString();
			
			logger.info("\n In Operator  Generated Sql Query?= " + sql + "\n");
			
			resObj = stmtObj.executeQuery(sql);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id") + ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname"));
				} while (resObj.next());
				//logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public static void likeOperator() {
		SelectQuery selectQuery = new SelectQuery();
		String sqllike = selectQuery.addCustomColumns(new CustomSql("id")).addCustomFromTable("stdtbl").addCondition(BinaryCondition.like(new CustomSql("fname"),"s%")).validate().toString();
		try {
			String sql = sqllike;//.replace("'", "");
			logger.info("\n like Operator  Generated Sql Query?= " + sql + "\n");
			
			resObj = stmtObj.executeQuery(sql);
			if (!resObj.next()) {
				logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id")); //+ ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname"));
				} while (resObj.next());
				logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  static void groupBy() {
		
		SelectQuery selectQuery = new SelectQuery();
		selectQuery.addAllColumns();
		selectQuery.addCustomFromTable("stdtbl");
		selectQuery.addCustomColumns(FunctionCall.countAll());
		selectQuery.addCustomGroupings(new CustomSql("city"));
		
		String groupBy = selectQuery.validate().toString();
		
		try {
			String sql = groupBy;//.replace("'", "");
			logger.info("\n GroupBy Operator  Generated Sql Query?= " + sql + "\n");
			
			resObj = stmtObj.executeQuery(sql);
			if (!resObj.next()) {
				//logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
			} else {
				do {
					logger.info("\nId= " + resObj.getString("id")); //+ ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname"));
				} while (resObj.next());
			//	logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void having() {
	SelectQuery selectQuery = new SelectQuery();
	selectQuery.addAllColumns();
	selectQuery.addCustomFromTable("stdtbl");
	selectQuery.addCustomColumns(FunctionCall.countAll());
	selectQuery.addCustomGroupings(new CustomSql("city"));
	selectQuery.addHaving(new ComboCondition(BinaryCondition.lessThan(new CustomSql("id"), 105)));
		String havingsql = selectQuery.validate().toString();
		try {
		logger.info("\n havingsql   Generated Sql Query?= " + havingsql + "\n");
		
		resObj = stmtObj.executeQuery(havingsql);
		if (!resObj.next()) {
			//logger.info("\n=======No Records Are Present In The '" + TABLE_NAME + "'=======\n");
		} else {
			do {
				logger.info("\nId= " + resObj.getString("id")); //+ ", First Name= " + resObj.getString("fname")+ ", last Name= " + resObj.getString("lname"));
			} while (resObj.next());
		//	logger.info("\n=======All Records Displayed From The '" + TABLE_NAME + "'=======\n");
		}
	
	} catch (Exception e) {
		e.printStackTrace();
	}
		
		
	}
}
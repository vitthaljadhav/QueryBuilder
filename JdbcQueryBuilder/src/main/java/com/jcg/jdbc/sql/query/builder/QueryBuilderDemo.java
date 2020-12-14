package com.jcg.jdbc.sql.query.builder;

import java.util.Random;

public class QueryBuilderDemo {

	public static void main(String[] args) {

		// Method #1 :: This Method Is Used To Connect With The Database
		Querybuilder.connectDb();

		// Method #2 :: This Method Is Used To Create A Database Table Using SQLQueryBuilder Utility
		//Querybuilder.createDbTable();

		// Method #3 :: This Method Is Used To Insert Records In A Table Using SQLQueryBuilder Utility
		/*for(int count = 101; count < 106; count++) {
			int randomSalary = 1000 + new Random().nextInt(500);
			Querybuilder.insertDataInTable(count, "Editor" + count, randomSalary);
		}*/

		//  Method #4 :: This Method Is Used To Display All Records From The Table Using SQLQueryBuilder Utility
		//Querybuilder.displayRecords();

		// Method #5 :: This Method Is Used To Display A Specific Record From The Table Using SQLQueryBuilder Utility
	//	Querybuilder.displaySelectiveRecord(103);

		// Method #6 :: This Method Is Used To Update A Record In A Table Using SQLQueryBuilder Utility
//		Querybuilder.updateRecord(101);

		// Method #7 :: This Method Is Used To Delete A Specific Record From The Table Using SQLQueryBuilder Utility
	//	Querybuilder.deleteSelectiveRecord(103);

		//Querybuilder.displayRecords();

		// Method #8 :: This Method Is Used To Delete All Records From The Table Using SQLQueryBuilder Utility
		//Querybuilder.deleteRecords();

	//	Querybuilder.displayRecords();

		// Method #9 :: This Method Is Used To Drop A Table From The Database Using SQLQueryBuilder Utility
		//Querybuilder.dropTableFromDb();

		// Method #10 :: This Method Is Used To Disconnect From The Database Using SQLQueryBuilder Utility
		
		//Ordering Operation
		//Querybuilder.getStudents();
		
		//Inner Join
		//Querybuilder.innerJoin();
		

		//left Join
		//Querybuilder.leftJoin();
		

		//Right Join
		//Querybuilder.rightJoin();
		

		//Full Join
		//Querybuilder.fullJoin();

		
		//Querybuilder.inOperator();
		
		//Querybuilder.likeOperator();
		
		//Querybuilder.groupBy();
		
		Querybuilder.having();
		
		Querybuilder.disconnectDb();
	}
}
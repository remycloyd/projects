package viewsNShit;

import java.sql.*;
import java.io.*;



/* There are two parts of this project, for which you will use the tables that you created for Project 1.

1. You need to modify and run the attached jdbcMySQL.java file to retrieve the results of the query:
Display, without duplicates, users who have a friend of a different gender.

(Note that you have to put in your username and password and change the query written in the attached file.)
You can compile and execute the jdbcMySQL.java file using Eclipse. 
You need to include the provided mysql-connector-java-8.0.13.jar file into your Java Build Path. 
Submit the modified file as well as a file containing the result obtained from the database for the query.

2. Create two views, one updatable and the other not updatable. Present sample queries and results on both views. 
Update the table with the updatable views and show before and after contents of the table. 
Submit a file containing the views, the update commands and the contents of the table before and after updates.*/

class jdbcMySQL
{
	public static void displayResult(ResultSet rset) throws SQLException 
	{
		
	    // Go through the records one at a time...
	    int i = 0;
	    while (rset.next ()){
	    	String row = rset.getString (1);
	    	System.out.println(" " + row);       
	    	i++;
	     }
	    System.out.println("\nNumber of records fetched: " + i +" \n");
	    //System.out.println("\n Part 2: creating views");
	}
	
	
	public static void main(String[] args) 
	{
		Connection conn = null;
		try 
		{
		    conn = DriverManager.getConnection("jdbc:mysql://129.244.40.38:3306/user10","user10", "Tuhurricane-finland");
		    // more processing here
		    Statement stmt = conn.createStatement (); // Create a Statement
		    
		    
		    // Part 1
		    System.out.println("part 1: Printing users with friends of opposite Gender.");
		    // Replace the following query text with the SQL query corresponding to the query in the assignment 
		    String qry = "select distinct user1.name \n" + 
		    		"from friends as frnds\n" + 
		    		"join users as user1 on frnds.id1 = user1.userid\n" + 
		    		"join users as user2 on frnds.id2 = user2.userid\n" + 
		    		"where user1.gender <> user2.gender;";
		    
		    // All the records after executing "qry" are fetched a ResultSet rset.
		    ResultSet rset = stmt.executeQuery (qry);
		    stmt = conn.createStatement();
		    rset = stmt.executeQuery(qry);
		    displayResult(rset);
	
		    
		    //Part 2
		    //reset views
		    System.out.println("Part 2 creating views: ");    
		    String dropView = "DROP VIEW if exists \n" + 
		    		"user10.friendsOfHale, user10.foH;";
		    String Reset = "delete from Friends where id1 =\"jHale\" and id2= \"mPapa\";";
		    stmt = conn.createStatement();
		    stmt.executeUpdate(dropView);
		    stmt.executeUpdate(Reset);
		    
		    
		    //create updateable view
		    String createUpdateable = "CREATE VIEW friendsOfHale AS\n" + 
		    		"SELECT Name, id2, id1, startdate \n" + 
		    		"FROM friends as f, Users as u\n" + 
		    		"where id1='jHale' and f.id2 = u.userid;";
		    stmt = conn.createStatement();
		    stmt.executeUpdate(createUpdateable);
		    System.out.println("\n Updateable view created showing friends of user jHale:");
		    
		    
		    //show updateable view
		    String showView1 = "select *\n" + 
		    		"		    from friendsofhale;";
		    stmt = conn.createStatement();
		    rset = stmt.executeQuery(showView1);
		    displayResult(rset);
		    
		    
		    //update view 
		    System.out.println("adding friend to list. \n");
		    String UpdateView1 = "INSERT INTO friendsofhale(id1, id2, startDate) "
		    		+ "VALUES (\"jHale\", \"mPapa\", \"2019-11-09\");";
		    stmt = conn.createStatement();
		    stmt.executeUpdate(UpdateView1);
		    
		    
		    // display updates to view
		    System.out.println("Displaying update to friends of JHale");
		    String showView1b = "select *\n" + 
		    		"		    from friendsofhale;";
		    stmt = conn.createStatement();
		    rset = stmt.executeQuery(showView1b);
		    displayResult(rset);
		   
		    
		    
		    
		    
		    //create Non updateable view
		    String createNonUpdateable = "CREATE VIEW foh AS\n" + 
		    		"SELECT distinct Name, id2, id1, startdate \n" + 
		    		"FROM friends as f, Users as u\n" + 
		    		"where id1='jHale' and f.id2 = u.userid;";
		    stmt = conn.createStatement();
		    stmt.executeUpdate(createNonUpdateable);
		    
		    //display view
		    System.out.println("NON-Updateable view created showing friends of user jHale.");
		    String showView2 = "select *\n" + 
		    		"		    from foH;";	    
		    stmt = conn.createStatement();
		    rset = stmt.executeQuery(showView2);
		    displayResult(rset);
		    
		    //attempt to delete newest entry from view 
		    System.out.println("attempting to delete friend from Non Updateable View:");
		    String UpdateView2 ="delete from foh where id2= 'mPapa';";
		    stmt = conn.createStatement();
		    stmt.executeUpdate(UpdateView2);
		    
		    
		  //display view
		    System.out.println("updates to non Updateable View: ");
		    String showView2b = "select distinct name from foH;";
		    stmt = conn.createStatement();
		    rset = stmt.executeQuery(showView2b);
		    displayResult(rset);
		    
		} 
		catch(SQLException e) 
		{
		   System.out.println(e.getMessage());
		} 
		finally 
		{
		 try
		 {
           if(conn != null)
             conn.close();
		 }
		 catch(SQLException ex)
		 {
		   System.out.println(ex.getMessage());
		 }
		}
	}
}
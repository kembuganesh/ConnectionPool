package com.connectionpool1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


public class ConnPoolServlet1 extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
		
		
		
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		PrintWriter pw=null;
		String tabName=null;
		ResultSetMetaData rsmd=null;
		int colCnt=0 ;
		//general settings
		pw=res.getWriter();
		//set response type
		res.setContentType("text/html");
		//read form data
		tabName=req.getParameter("table");
		//write jdbc code  for remaining work
		try {
			//get con object from jdbc con pool
			con=makeConnection();
			//create statement
			st=con.createStatement();
			//send and execute sql query
			rs=st.executeQuery("select * from"+" "+tabName);
			//get resultset metadata																																																																																																					
			rsmd=rs.getMetaData();
			//print col names
			colCnt=rsmd.getColumnCount();
			pw.println("<table border='1' bgcolor='red'>");
			pw.println("<tr bgcolor='cyan'>");
			for(int i=1;i<=colCnt;i++) {
				pw.println("<th>"+rsmd.getColumnLabel(i)+"<th>");
			}
			pw.println("</tr>");
			//process the result
			while(rs.next()) {
				pw.println("<tr>");
				for(int i=1;i<=colCnt;i++) {
					pw.println("<td>"+rs.getString(i)+"</td>");
				}
				pw.println("</tr>");
			}
			pw.println("</table>");
			pw.println("<br><a href='index.html'>try again</a>");
			//close jdbc objects
			rs.close();
			st.close();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
			pw.println("<b><center>internal problem</center></b>");
			pw.println("<br><a href='index.html'>try again</a>");
			
		}
		pw.close();
		
		
		}
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
		doGet(req,res);
	}

	private Connection makeConnection() {
		Connection con=null;
		InitialContext ic=null;
		DataSource ds=null;
		try {
			//locate jndi registry
			ic=new InitialContext();
			//get datasource object from jndi registry
			ds=(DataSource)ic.lookup("java:/oracleDS");
			//get JDBC con object from JDBC con pool
			con=ds.getConnection();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

}

package student_details.jdbc.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jdk.nashorn.internal.runtime.ECMAException;

import java.sql.PreparedStatement;

public class StudentDbUtil {
	
	private DataSource datasource;
	
	public StudentDbUtil(DataSource thedatasource) {
		datasource = thedatasource;
	}
	
	public List<Student> getStudents() throws Exception{
		
		List<Student> students = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null ;
		ResultSet rs = null ;
		
		try {
			
			con = datasource.getConnection();
			
			String sql = "select * from student order by last_name";
			
			stmt = con.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				Student tempStudent = new Student(id,firstName,lastName,email);
				
				students.add(tempStudent);
			}
			return students;
		}
		finally {
			close(con,stmt,rs);
		}
	}

	private void close(Connection con, Statement stmt, ResultSet rs) {
		
		try {
			if(rs != null) {
				rs.close();
			}
			
			if(stmt != null) {
				stmt.close();
			}
			
			if(con != null) {
				con.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception{
		
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = datasource.getConnection();
			
			String sql = "insert into student "
						+ "(first_name, last_name, email) "
						+ "values(?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, theStudent.getFirstName());
			pstmt.setString(2, theStudent.getLastName());
			pstmt.setString(3, theStudent.getEmail());
			
			pstmt.execute();
		}finally {
			close(con, pstmt, null);
		}
	}

	public Student getStudent(String studentId) throws Exception{
		
		Student theStudent = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int studentID;
		
		try {
				studentID = Integer.parseInt(studentId);
				
				con = datasource.getConnection();
				
				String sql = "select * from student where id=?";
				
				pstmt = con.prepareStatement(sql);
				
				pstmt.setInt(1, studentID);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					String email = rs.getString("email");
					
					theStudent = new Student(studentID, firstName, lastName, email);
				}
				else {
					throw new Exception("Could not find student id: " + studentID);
				}
		}finally {
			close(con, pstmt, rs);
			}
		
		return theStudent;
	}

	public void updateStudent(Student theStudent) throws Exception{
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
		con = datasource.getConnection();
		
		String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
		
		pstmt = con.prepareStatement(sql);
		
		pstmt.setString(1, theStudent.getFirstName());
		pstmt.setString(2, theStudent.getLastName());
		pstmt.setString(3, theStudent.getEmail());
		pstmt.setInt(4, theStudent.getId());
		
		pstmt.execute();
		
		}finally {
			close(con, pstmt, null);
			
		}
	}

	public void deleteStudent(String studentid) throws Exception{
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			int studId = Integer.parseInt(studentid);
			
			con = datasource.getConnection();
			
			String sql = "delete from student where id=?";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, studId);
			
			pstmt.execute();
		}finally {
			close(con, pstmt, null);
		}
	}

	public List<Student> searchStudents(String theSearchName) throws Exception{
		
		List<Student> students = new ArrayList<>();
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int studentId;
        
        try {
            
             con = datasource.getConnection();
            
            if (theSearchName != null && theSearchName.trim().length() > 0) {
                
                String sql = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
                
                pstmt = con.prepareStatement(sql);
                
                String theSearchNameLike = "%" + theSearchName.toLowerCase() + "%";
                pstmt.setString(1, theSearchNameLike);
                pstmt.setString(2, theSearchNameLike);
                
            } else {
                
                String sql = "select * from student order by last_name";
                
                pstmt = con.prepareStatement(sql);
            }
            
            
            rs = pstmt.executeQuery();
            
           
            while (rs.next()) {
                
                
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                
                Student tempStudent = new Student(id, firstName, lastName, email);
                
                
                students.add(tempStudent);            
            }
            
            return students;
        }
        finally {
            
            close(con, pstmt, rs);
        }
	}
}

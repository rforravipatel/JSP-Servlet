package student_details.jdbc.crud;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			studentDbUtil = new StudentDbUtil(datasource);
		}catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String theCommand = request.getParameter("command");
			
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			switch(theCommand) {
			
			case "LIST": 
				listStudents(request,response);
				break;
				
			case "ADD":
				addStudent(request,response);
				break;
				
			case "LOAD":
				loadstudent(request,response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request,response);
				
			case "SEARCH":
                searchStudents(request, response);
                break;
                
			default:
				listStudents(request,response);
			}
		
		}catch(Exception e) {
			throw new ServletException(e);
		}
	}

	private void searchStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String theSearchName = request.getParameter("theSearchName");
        
        
        List<Student> students = studentDbUtil.searchStudents(theSearchName);
        
        
        request.setAttribute("student_list", students);
   
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list_students.jsp");
        dispatcher.forward(request, response);
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String studentid = request.getParameter("studentId");
		
		studentDbUtil.deleteStudent(studentid);
		
		listStudents(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(id,firstName,lastName,email);
		
		studentDbUtil.updateStudent(theStudent);
		
		listStudents(request,response);
		
	}

	private void loadstudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String studentId = request.getParameter("studentId");
		
		Student thestudent = studentDbUtil.getStudent(studentId);
		
		request.setAttribute("THE_STUDENT", thestudent);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update_student_form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(firstName,lastName,email);
		
		studentDbUtil.addStudent(theStudent);
		
		listStudents(request,response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<Student> students = studentDbUtil.getStudents();
		
		request.setAttribute("student_list", students);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list_students.jsp");
		dispatcher.forward(request, response);
	}

}

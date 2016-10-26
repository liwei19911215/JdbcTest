package dao.interfaces;

import bean.Student;

public interface IStudentDao {
	public Student[] getStudentById(String id) throws Exception;
}

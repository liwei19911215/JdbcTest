package service.impl;

import service.interfaces.IStudentSV;
import Factory.ServiceFactory;
import bean.Student;
import dao.impl.StudentDaoImpl;
import dao.interfaces.IStudentDao;

public class StudentImpl implements IStudentSV {

	public Student[] getStudentById(String id) throws Exception {
		IStudentDao studentDao = ServiceFactory.getService(StudentDaoImpl.class);
		return studentDao.getStudentById(id);
	}

}

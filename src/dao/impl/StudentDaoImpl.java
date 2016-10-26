package dao.impl;

import java.util.HashMap;
import java.util.Map;

import bean.Student;

import Utils.DaoUtil;
import Utils.StringUtil;
import dao.interfaces.IStudentDao;

public class StudentDaoImpl implements IStudentDao {

	public Student[] getStudentById(String id) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();
		if(!StringUtil.isEmpty(id)){
			sql.append("student_id = :id");	
			params.put("id", id);
		}
		Student[] s = DaoUtil.query("student",sql.toString(), params,Student.class);
		return s;
	}

}

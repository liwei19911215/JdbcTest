package bean;

public class Student {
	private long studentId;
	private String studentName;
	private long age;
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public long getStudentId() {
		return studentId;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getAge() {
		return age;
	}
	
	public String toString(){
		return "Student [id = "+studentId+" | name = "+studentName+" | age = "+age+"]";
	}
}

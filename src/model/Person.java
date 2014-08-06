package model;

public class Person {

	private int id;
	private String name;
	private String phone;
	private String addr;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", addr=" + addr + "]";
	}
	
	
	public static void main(String[] args) {
		Person person=new Person();
		person.setId(3);
		person.setName("Peter");
		person.setPhone("54848");
		System.out.println(person);
	}
	
}

package com.example.demo.model;


import java.io.Serializable;

import javax.persistence.*;
@Entity
@Table(name = "customers")
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "email")
	private String email;



	public Customer() {
	}
	public Customer(String name, String surname, String email) {
		this.name = name;
		this.surname = surname;
		this.email = email;
	}
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	@Override
	public String toString() {
		return "Menu [name=" + name + ", surname=" + surname + ", email=" + email + "]";
	}

}

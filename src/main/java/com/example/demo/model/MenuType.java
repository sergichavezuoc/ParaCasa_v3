package com.example.demo.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "menustype")
public class MenuType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "type")
	private String type;

	public MenuType() {
	}

	public MenuType(String type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "MenuType [id=" + id + ", type=" + type + "]";
	}
}

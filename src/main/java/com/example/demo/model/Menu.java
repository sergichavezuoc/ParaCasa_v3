package com.example.demo.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "menus")
public class Menu implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	}, mappedBy = "menus")
	@JsonIgnore
	private Set<Order> orders = new HashSet<>();
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tutorials_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Restaurant restaurant;

	public Menu() {
	}

	public Menu(String name, String description, Restaurant restaurant) {
		this.name = name;
		this.description = description;
		this.restaurant = restaurant;
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

	public void setMenu(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public String getDescription() {
		return description;
	}

	public String getRestaurant() {
		return restaurant.getName();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", desc=" + description + ", restaurant=" + restaurant.getName()
				+ "]";
	}
}

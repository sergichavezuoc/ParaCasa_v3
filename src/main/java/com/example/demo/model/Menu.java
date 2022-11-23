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
	@Column(nullable = true, length = 64)
    private String image;


	public Menu() {
	}

	public Menu(String name, String description, Restaurant restaurant, String image) {
		this.name = name;
		this.description = description;
		this.restaurant = restaurant;
		this.image = image;
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

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	@Transient
    public String getPhotosImagePath() {
        if (this.image == null || this.name == null) return null;
         
        return "/image"  + "/" + this.image;
    }

	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", desc=" + description + ", restaurant=" + restaurant.getName()
				+ "]";
	}
}

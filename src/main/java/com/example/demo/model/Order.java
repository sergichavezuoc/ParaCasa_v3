package com.example.demo.model;

import java.io.Serializable;
import java.lang.annotation.Repeatable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "date_added")
	private LocalDateTime dateAdded;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "orders_menus", joinColumns = { @JoinColumn(name = "menu_id") }, inverseJoinColumns = {
			@JoinColumn(name = "order_id") })
	private Set<Menu> menus = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	public Order() {
	}

	public Order(User user) {
		this.user = user;
		this.dateAdded = LocalDateTime.now();
	}

	public void addMenu(Menu menu) {
		this.menus.add(menu);
		menu.getOrders().add(this);
	}

	public long getId() {
		return id;
	}

	public LocalDateTime getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		dateAdded = this.dateAdded;
	}

	public void removeMenu(Menu menu) {
		this.menus.remove(menu);
		menu.getOrders().remove(this);
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	public Long getCustomerId() {
		return user.getId();
	}

	public String getCustomerName() {
		return user.getName();
	}

	public String getCustomerSurname() {
		return user.getSurname();
	}

	@Override
	public String toString() {

		return "Order [id=" + id + ", date=" + dateAdded + ", customer=" + user.getName() + "]";
	}

}

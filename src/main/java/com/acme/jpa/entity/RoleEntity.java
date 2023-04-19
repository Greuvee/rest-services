package com.acme.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class RoleEntity  implements Serializable {

	private static final long serialVersionUID = -7439754902834396418L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column
    private String name;
    
	@ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<UserEntity> users;
	
    public RoleEntity() {
		super();
	}
    
    public RoleEntity(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<UserEntity> getUsers() {
		if(this.users == null) {
			return new ArrayList<>();
		}else {
			return this.users;
		}
	}

	public void setUsers(Collection<UserEntity> users) {
		if(users == null) {
			this.users = new ArrayList<>();
		}else {
			this.users = users;
		}
	}
	
	public void addUser(UserEntity user) {
		getUsers().add(user);
	}
    
}

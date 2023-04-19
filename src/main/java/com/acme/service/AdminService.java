package com.acme.service;

import com.acme.exception.RoleNotFoundException;

public interface AdminService {

	void assignAdvocateRoleToUser(String emailAddress) throws RoleNotFoundException;
	
}

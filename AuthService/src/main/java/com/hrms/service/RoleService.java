package com.hrms.service;

import com.hrms.repository.IRoleRepository;
import com.hrms.repository.entity.Role;
import com.hrms.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends ServiceManager<Role, Long> {
    private final IRoleRepository roleRepository;
    public RoleService(IRoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    public String getRoleNames(Long roleId){
        System.out.println("role servise geldi");
        System.out.println(findById(roleId).get());
        System.out.println(findById(roleId).get().getRoleName());
       return findById(roleId).get().getRoleName();
    }
}

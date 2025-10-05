package sga.sgajwt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sga.sgajwt.Models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

}

package org.tutorial.clique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.clique.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
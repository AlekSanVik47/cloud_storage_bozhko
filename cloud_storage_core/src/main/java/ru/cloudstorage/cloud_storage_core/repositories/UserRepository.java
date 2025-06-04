package ru.cloudstorage.cloud_storage_core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cloudstorage.cloud_storage_core.entitities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}

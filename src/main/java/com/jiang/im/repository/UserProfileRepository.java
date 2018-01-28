package com.jiang.im.repository;

import com.jiang.im.dataobject.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,String> {
}

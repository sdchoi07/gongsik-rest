package com.gongsik.gsr.api.mypage.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.profile.entity.ProfileEntity;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String>{

	Optional<ProfileEntity> findByUsrId(String string);

	Optional<ProfileEntity> findByUsrIdAndLogTp(String usrId, String logTp);



}

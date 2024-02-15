package com.gongsik.gsr.api.mypage.uscart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;

@Repository
public interface CartRepository  extends JpaRepository<CartEntity, Long>{

	List<CartEntity> findByCartUsrIdAndCartStAndDelYn(String usrId, String cartSt, String string);

}

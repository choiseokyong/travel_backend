package com.travel.service;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.domain.MyUser;
import com.travel.mapper.MyUserMapper;

@Service
public class MyUserService implements UserDetailsService{
	private final MyUserMapper usermapper;
	private final PasswordEncoder passwordencoder;	// 주입
	
	
	public MyUserService(MyUserMapper usermapper,@Lazy PasswordEncoder passwordencoder) {
		this.usermapper = usermapper;
		this.passwordencoder = passwordencoder;
		
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		/*
		 * 이 메서드는 로그인 시 Security가 DB에서 사용자 정보를 가져오는 진입점.
		 * 로그인 성공/실패는 Spring Security가 알아서 처리함.
		 * 직접 로그인 검증하지 않고, Security에게 필요한 정보를 제공해주는 역할.
		 * 
		 */
		
        MyUser user = usermapper.findByEmail(email); // DB에서 사용자를 조회
        
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);		// 사용자 없을 경우 예외 던짐
        }
        
        String role = convertGradeToRole(user.getGrade());

        // 여기서 비밀번호 비교 직접 하는 게 아니라, Spring Security가 authenticate()에서 해줌
        // 하지만 만약 직접 비교가 필요하면 passwordEncoder.matches(raw, encoded) 사용 가능
        try {
            UserDetails userDetails = User.builder() // Security가 사용할 UserDetails 객체 생성
                .username(user.getEmail())	// 로그인 시 비교할 username 설정
                .password(user.getPassWord())	// 암호화된 비밀번호 그대로 넣기 ( Security가 비교함)
                .roles(role)	// 권한 설정
                .build();
            System.out.println("userDetails: " + userDetails);
            return userDetails;
        } catch (Exception e) {
            e.printStackTrace();  // 전체 예외 메시지 보기
            throw new RuntimeException("UserDetails 생성 중 오류 발생", e);
        }
    }
	
	private String convertGradeToRole(int grade) {
	    switch (grade) {
	    	case 1: return "ADMIN";
	        case 5: return "USER";
	        default: return "GUEST";
	    }
	}
	
	public List<MyUser> getUserByNo(){
		return usermapper.getUserByNo();
		
	}
	
	public List<MyUser> getUserByUserNo(int userNo){
		return usermapper.getUserByUserNo(userNo);
	}
	
	public int createUser(MyUser user) {
		MyUser myuser= usermapper.findByEmail(user.getEmail());
		if (myuser != null) {
	        throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
	    }
		user.setPassWord(passwordencoder.encode(user.getPassWord()));
		return usermapper.insertUser(user);
	}
	
	public int modifyUser(MyUser user) {
		return usermapper.updateUser(user);
	}
	
	public int deleteUser(int userNo) {
		return usermapper.deleteUser(userNo);
	}
}

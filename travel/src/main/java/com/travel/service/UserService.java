package com.travel.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.domain.LoginRequest;
import com.travel.domain.LoginResponse;
import com.travel.domain.MyUser;
import com.travel.mapper.UserMapper;

@Service
public class UserService implements UserDetailsService{
	private final UserMapper usermapper;
	private final PasswordEncoder passwordencoder;	// 주입
	private final AuthenticationManager authenticationManager; // 여기에서 주입받기
	
	public UserService(UserMapper usermapper,PasswordEncoder passwordencoder,AuthenticationManager authenticationManager) {
		this.usermapper = usermapper;
		this.passwordencoder = passwordencoder;
		this.authenticationManager = authenticationManager;
	}
	
	// 로그인 검증 로직
	public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate( 	//사용자 검증 전체 진행
            new UsernamePasswordAuthenticationToken(	// 로그인 요청 객체
                request.getEmail(),
                request.getPassword()
                
            )
        );
//        LoginResponse token = jwtTokenProvider.generateToken(authentication);
//        return token;
        return null;
    }
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginRequest user = usermapper.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }

        // 여기서 비밀번호 비교 직접 하는 게 아니라, Spring Security가 authenticate()에서 해줌
        // 하지만 만약 직접 비교가 필요하면 passwordEncoder.matches(raw, encoded) 사용 가능

        return null;
//        return User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword()) // 암호화된 비밀번호 그대로 넣기
//                .roles(user.getRole())
//                .build();
    }
	
	public List<MyUser> getUserByNo(){
		return usermapper.getUserByNo();
		
	}
	
	public List<MyUser> getUserByUserNo(int userNo){
		return usermapper.getUserByUserNo(userNo);
	}
	
	public int createUser(MyUser user) {
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

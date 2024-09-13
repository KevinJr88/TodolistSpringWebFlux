package net.java.webflux_security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LoginResponse {
	
	private String token;
	private String email;

	@Override
	public String toString() {
		return "LoginResponse{" +
				"token='" + token + '\'' +
				"email='" + email + '\'' +
				'}';
	}
}

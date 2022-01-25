package com.politech.core.auth.rest.dto.users;

import com.politech.core.auth.domain.model.users.UserAccountState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetAllUsersRequest
{
	private String name;
	private String email;
	private String phoneNumber;
	private boolean emailExactMatch;
	private Integer invalidLoginAttempts;
	private UserAccountState accountState;
	private Boolean emailVerified;
	private String userCompanyName;
	private String locale;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdFrom;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdTo;
	private LocalDateTime updatedAfter;
}

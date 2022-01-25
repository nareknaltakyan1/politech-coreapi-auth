package com.politech.core.auth.domain.service.users;

import com.politech.core.auth.domain.model.organizations.Organization;
import com.politech.core.auth.domain.model.users.*;
import com.politech.core.auth.domain.service.organizations.OrganizationService;
import com.politech.core.auth.domain.service.system.SystemDateTimeService;
import com.politech.core.auth.infrastructure.UserRegisteredEvent;
import com.politech.core.auth.infrastructure.common.validation.UniqueConstraintException;
import com.politech.core.auth.rest.exception.AuthErrorField;
import com.politech.core.auth.security.PasswordBasedKeyPasswordEncoder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("PMD.TooManyMethods")
@Service
public class UserService
{
	private static final String LIKE_PATTERN = "%%%s%%";

	private final ApplicationEventPublisher eventPublisher;

	private final UserRepository repository;

	private final UserRoleRepository userRoleRepository;

	private final PasswordBasedKeyPasswordEncoder passwordEncoder;

	private final OrganizationService organizationService;

	private final SystemDateTimeService systemDateTimeService;

	private final Integer failedLoginAttemptsLimit;

	public UserService(final ApplicationEventPublisher eventPublisher, final UserRepository repository, final UserRoleRepository userRoleRepository,
		final PasswordBasedKeyPasswordEncoder passwordEncoder, final OrganizationService organizationService,
		final SystemDateTimeService systemDateTimeService, @Value("${internal.failed-login-attempts-limit}") final Integer failedLoginAttemptsLimit)
	{
		this.eventPublisher = eventPublisher;
		this.repository = repository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
		this.organizationService = organizationService;
		this.systemDateTimeService = systemDateTimeService;
		this.failedLoginAttemptsLimit = failedLoginAttemptsLimit;
	}

	@Transactional(readOnly = true)
	public Page<User> findAll(final GetAllUsersDetails getAllUsersPojo, final Pageable pageable)
	{
		final BooleanBuilder filter = buildFilter(getAllUsersPojo);
		return repository.findAll(filter, pageable);
	}

	@Transactional(readOnly = true)
	public Page<User> findAllByPermission(final Permission permission, final Pageable pageable)
	{
		Assert.notNull(permission, "User permission should not be null");
		Assert.notNull(pageable, "Pageable request should not be null");
		return repository.findAll(buildFilter(permission), pageable);
	}

	@Transactional(readOnly = true)
	public User findById(final Long id)
	{
		final Optional<User> user = repository.findById(id);
		if (user.isEmpty())
		{
			throw new EntityNotFoundException(String.format("Could not find user with id '%d'", id));
		}
		return user.get();
	}

	@Transactional(readOnly = true)
	public User findByEmail(final String email)
	{
		final Optional<User> user = repository.findByEmail(email);
		if (user.isEmpty())
		{
			throw new UserNotExistsForEmailException(email, String.format("Could not find user with email '%s'", email));
		}
		return user.get();
	}

	@Transactional(readOnly = true)
	public boolean userExistsByEmail(final String email)
	{
		return repository.existsByEmail(email.toLowerCase(Locale.getDefault()));
	}

	@Transactional(readOnly = true)
	public boolean userExistsByPhoneNumber(final String phoneNumber)
	{
		return repository.existsByPhoneNumber(phoneNumber);
	}

	@Transactional
	public User createUser(final CreateUserDetails userDetails)
	{
		User user = new User();
		user.setName(userDetails.getName());
		if (Objects.nonNull(userDetails.getEmail()))
		{
			user.setEmail(StringUtils.trim(userDetails.getEmail().toLowerCase(Locale.ROOT)));
		}
		user.setPhoneNumber(userDetails.getPhoneNumber());
		user.setAccountState(UserAccountState.ACTIVE);
		user.setLocale(userDetails.getLocale());
		final String password = extractPassword(userDetails);
		user.setSaltedPasswordHash(passwordEncoder.encode(password));
		user.setCreated(systemDateTimeService.getCurrentDateTime());
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		final GlobalUserPermission ur = new GlobalUserPermission();
		ur.setCreated(systemDateTimeService.getCurrentDateTime());
		ur.setPermission(Permission.READ_NON_SENSITIVE);
		ur.setUser(user);
		user.setPermissions(new ArrayList<>(List.of(ur)));
		user = repository.save(user);
		eventPublisher.publishEvent(UserRegisteredEvent.of(user.getId()).withRawPassword(password)
			.withWaitForDataSyncCompletion(userDetails.isWaitForSync()).withSendEmail(userDetails.isSendEmail()));
		return user;
	}

	@Transactional
	public void recordInvalidLoginAttempt(final String email, final String userAgent, final String ipAddress)
	{
		final User user = findByEmail(email);
		final FailedLoginAttempt failedLoginAttempt = new FailedLoginAttempt();
		failedLoginAttempt.setUserAgent(userAgent);
		failedLoginAttempt.setIpAddress(ipAddress);
		failedLoginAttempt.setDate(systemDateTimeService.getCurrentDateTime());
		user.addFailedLoginAttempt(failedLoginAttempt);
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(user);
		if (user.getFailedLoginAttempts().size() == failedLoginAttemptsLimit)
		{
			user.lockAccount();
		}
	}

	@Transactional
	public void updateLastLogin(final Long userId, final LocalDateTime lastLogin)
	{
		final User user = findById(userId);
		user.setLastLogin(lastLogin);
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		user.clearAllFailedLoginAttempts();
		repository.save(user);
	}

	@Transactional
	public void updateUser(final Long userId, final UpdateUserDetails details)
	{
		final User user = findById(userId);
		if (Objects.nonNull(user.getEmail()) && !user.getEmail().equals(details.getEmail()) && userExistsByEmail(details.getEmail()))
		{
			throw new UniqueConstraintException(AuthErrorField.EMAIL.getCode());
		}
		if (Objects.nonNull(user.getPhoneNumber()) && !user.getPhoneNumber().equals(details.getPhoneNumber())
			&& userExistsByPhoneNumber(details.getPhoneNumber()))
		{
			throw new UniqueConstraintException(AuthErrorField.PHONE_NUMBER.getCode());
		}
		user.setName(details.getName());
		user.setLocale(details.getLocale());
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		final String email = StringUtils.trim(details.getEmail().toLowerCase(Locale.ROOT));
		if (!user.getEmail().equals(email))
		{
			user.setEmail(email);
			user.setEmailVerified(false);
		}
		user.setPhoneNumber(details.getPhoneNumber());
		repository.save(user);
	}

	@Transactional
	public void updateAccountState(final Long userId, final UserAccountState state)
	{
		final User user = findById(userId);
		if (user.getAccountState() != state && state == UserAccountState.ACTIVE)
		{
			user.clearAllFailedLoginAttempts();
		}
		user.setAccountState(state);
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(user);
	}

	@Transactional
	public void changePassword(final Long userId, final String password)
	{
		final User user = findById(userId);
		user.setSaltedPasswordHash(passwordEncoder.encode(password));
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(user);
	}

	@Transactional
	public void grantGlobalRoleToUser(final Long userId, final Permission role)
	{
		final User user = findById(userId);
		if (userHasGlobalRole(user, role))
		{
			throw new IllegalArgumentException(String.format("User '%s' has already been granted role '%s'", userId, role));
		}
		if (user.getPermissions() == null)
		{
			user.setPermissions(new ArrayList<>());
		}
		final GlobalUserPermission ur = new GlobalUserPermission();
		ur.setCreated(systemDateTimeService.getCurrentDateTime());
		ur.setPermission(role);
		ur.setUser(user);
		user.getPermissions().add(ur);
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(user);
	}

	@Transactional
	public void grantOrganizationalPermissionToUser(final Long userId, final Long organizationId, final Permission role)
	{
		final User user = findById(userId);
		final Organization org = organizationService.findById(organizationId);
		if (userHasOrganizationalRole(user, org, role))
		{
			throw new IllegalArgumentException(
				String.format("User '%s' has already been granted role '%s' for organization '%d'", userId, role, organizationId));
		}
		if (user.getPermissions() == null)
		{
			user.setPermissions(new ArrayList<>());
		}
		final OrganizationalUserPermission ur = new OrganizationalUserPermission();
		ur.setCreated(systemDateTimeService.getCurrentDateTime());
		ur.setPermission(role);
		ur.setUser(user);
		ur.setOrganization(org);
		user.getPermissions().add(ur);
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(user);
	}

	@Transactional
	public void revokePermissionFromUser(final Long userId, final Long userRoleId)
	{
		final Optional<UserPermission> userRole = userRoleRepository.findById(userRoleId);
		if (userRole.isEmpty())
		{
			throw new EntityNotFoundException(String.format("Could not find user role with id '%d'", userRoleId));
		}
		final User user = userRole.get().getUser();
		if (!user.getId().equals(userId))
		{
			throw new IllegalStateException(String.format("User role %s does not belong to user %d", userRoleId, userId));
		}
		user.getPermissions().remove(userRole.get());
		user.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(user);
	}

	private boolean userHasGlobalRole(final User user, final Permission role)
	{
		for (final GlobalUserPermission r : user.getGlobalPermissions())
		{
			if (r.getPermission().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	private boolean userHasOrganizationalRole(final User user, final Organization organization, final Permission role)
	{
		for (final OrganizationalUserPermission r : user.getOrganizationalPermissions())
		{
			if (r.getPermission().equals(role) && r.getOrganization().getId().equals(organization.getId()))
			{
				return true;
			}
		}
		return false;
	}

	private BooleanBuilder buildFilter(final GetAllUsersDetails getAllUsersPojo)
	{
		Assert.notNull(getAllUsersPojo, "Argument getAllUsersPojo should not be null");
		final BooleanBuilder filter = new BooleanBuilder();
		Optional.ofNullable(getAllUsersPojo.getEmail()).filter(org.springframework.util.StringUtils::hasText).ifPresent(email -> filter
			.and(getAllUsersPojo.isEmailExactMatch() ? QUser.user.email.equalsIgnoreCase(email) : QUser.user.email.containsIgnoreCase(email)));
		Optional.ofNullable(getAllUsersPojo.getEmailVerified()).ifPresent(emailVerified -> filter.and(QUser.user.emailVerified.eq(emailVerified)));
		Optional.ofNullable(getAllUsersPojo.getName()).filter(org.springframework.util.StringUtils::hasText)
			.ifPresent(name -> filter.and(QUser.user.name.likeIgnoreCase(String.format(LIKE_PATTERN, name))));
		Optional.ofNullable(getAllUsersPojo.getInvalidLoginAttempts())
			.ifPresent(attempts -> filter.and(QUser.user.failedLoginAttempts.size().eq(attempts)));
		Optional.ofNullable(getAllUsersPojo.getAccountState())
			.ifPresent(userAccountState -> filter.and(QUser.user.accountState.eq(userAccountState)));
		Optional.ofNullable(getAllUsersPojo.getCreatedFrom()).ifPresent(createdFrom -> filter.and(QUser.user.created.goe(createdFrom)));
		Optional.ofNullable(getAllUsersPojo.getCreatedTo()).ifPresent(createdTo -> filter.and(QUser.user.created.loe(createdTo)));
		Optional.ofNullable(getAllUsersPojo.getLocale()).ifPresent(locale -> filter.and(QUser.user.locale.eq(locale)));
		Optional.ofNullable(getAllUsersPojo.getUpdatedAfter()).ifPresent(updatedAfter -> filter.and(QUser.user.updated.gt(updatedAfter)));
		addOrganizationPredicates(getAllUsersPojo, filter);
		return filter;
	}

	private BooleanBuilder buildFilter(final Permission permission)
	{
		final var filter = new BooleanBuilder();
		final List<BooleanExpression> predicates = new ArrayList<>();
		predicates.add(QUserPermission.userPermission.permission.eq(permission));
		final JPAQuery<Long> userIds = new JPAQuery<>().select(QUserPermission.userPermission.user.id).from(QUserPermission.userPermission)
			.where(predicates.toArray(Predicate[]::new));
		filter.and(QUser.user.id.in(userIds));
		return filter;
	}

	private void addOrganizationPredicates(final GetAllUsersDetails getAllUsersPojo, final BooleanBuilder filter)
	{
		final List<BooleanExpression> predicates = new ArrayList<>();
		Optional.ofNullable(getAllUsersPojo.getOrganizationId()).ifPresent(organizationId -> predicates
			.add(QOrganizationalUserPermission.organizationalUserPermission.organization.id.eq(getAllUsersPojo.getOrganizationId())));
		Optional.ofNullable(getAllUsersPojo.getUserCompanyName()).filter(org.springframework.util.StringUtils::hasText)
			.ifPresent(orgName -> predicates.add(QOrganizationalUserPermission.organizationalUserPermission.organization.name.eq(orgName)));
		if (CollectionUtils.isEmpty(predicates))
		{
			return;
		}
		final JPAQuery<Long> userIds = new JPAQuery<>().select(QOrganizationalUserPermission.organizationalUserPermission.user.id)
			.from(QOrganizationalUserPermission.organizationalUserPermission)
			.innerJoin(QOrganizationalUserPermission.organizationalUserPermission.organization).where(predicates.toArray(Predicate[]::new));
		filter.and(QUser.user.id.in(userIds));
	}

	private String extractPassword(final CreateUserDetails userDetails)
	{
		if (userDetails.isGeneratePassword())
		{
			return RandomStringUtils.randomAlphanumeric(12);
		}
		return userDetails.getPassword();
	}
}

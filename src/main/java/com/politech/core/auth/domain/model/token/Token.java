package com.politech.core.auth.domain.model.token;

import com.politech.core.auth.domain.model.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "token")
public abstract class Token
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false, insertable = false)
	private TokenType type;
	private String token;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	@Column(nullable = false)
	private LocalDateTime created;
	private LocalDateTime expiresAt;
	@Enumerated(EnumType.STRING)
	private TokenState state;
	@OneToMany(mappedBy = "token", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TokenStateChange> stateChanges;

	public Token()
	{
	}

	public Token(final TokenType type)
	{
		this.type = type;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(final String token)
	{
		this.token = token;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}

	public LocalDateTime getCreated()
	{
		return created;
	}

	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}

	public TokenState getState()
	{
		return state;
	}

	public void setState(final TokenState state)
	{
		this.state = state;
	}

	public List<TokenStateChange> getStateChanges()
	{
		return stateChanges;
	}

	public void setStateChanges(final List<TokenStateChange> stateChanges)
	{
		this.stateChanges = stateChanges;
	}

	public LocalDateTime getExpiresAt()
	{
		return expiresAt;
	}

	public void setExpiresAt(final LocalDateTime expiresAt)
	{
		this.expiresAt = expiresAt;
	}

	public TokenType getType()
	{
		return type;
	}
}

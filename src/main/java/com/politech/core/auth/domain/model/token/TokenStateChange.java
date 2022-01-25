package com.politech.core.auth.domain.model.token;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokenstatechange")
public class TokenStateChange
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tokenId")
	private Token token;
	@Enumerated(EnumType.STRING)
	private TokenState previousState;
	@Enumerated(EnumType.STRING)
	private TokenState newState;
	@Column(nullable = false)
	private LocalDateTime created;

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public Token getToken()
	{
		return token;
	}

	public void setToken(final Token token)
	{
		this.token = token;
	}

	public TokenState getPreviousState()
	{
		return previousState;
	}

	public void setPreviousState(final TokenState previousState)
	{
		this.previousState = previousState;
	}

	public TokenState getNewState()
	{
		return newState;
	}

	public void setNewState(final TokenState newState)
	{
		this.newState = newState;
	}

	public LocalDateTime getCreated()
	{
		return created;
	}

	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}
}

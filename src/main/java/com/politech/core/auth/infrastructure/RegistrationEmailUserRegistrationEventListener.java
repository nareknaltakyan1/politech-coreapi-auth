package com.politech.core.auth.infrastructure;

//import com.google.gson.JsonObject;
//import com.politech.core.auth.domain.service.users.UserService;
//import com.transferz.core.auth.domain.model.users.User;
//import com.transferz.core.auth.domain.service.users.UserService;
//import com.transferz.core.auth.infrastructure.communications.CommunicationService;
//import com.transferz.core.auth.infrastructure.communications.EmailRecipient;
//import com.transferz.core.common.domain.model.email.EmailType;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Lazy(false)
@Component
public class RegistrationEmailUserRegistrationEventListener
{
//	//todo create Communication Service
//	private final Object communicationService;
//
//	private final UserService userService;
//
//	@Value("${internal.from-name}")
//	private String fromName;
//
//	@Value("${internal.from-email}")
//	private String fromEmail;
//
//	public RegistrationEmailUserRegistrationEventListener(final CommunicationService communicationService, final UserService userService)
//	{
//		this.communicationService = communicationService;
//		this.userService = userService;
//	}
//
//	@Async
//	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//	public void onEvent(final com.transferz.core.auth.infrastructure.UserRegisteredEvent event)
//	{
//		if (!event.isSendEmail())
//		{
//			return;
//		}
//		final User user = userService.findById(event.getUserId());
//		final JsonObject data = createRequestData(event, user);
//		final EmailRecipient from = createRecipient(fromName, fromEmail);
//		final EmailRecipient to = createRecipient(user.getName(), user.getEmail());
//		communicationService.sendEmail(EmailType.USER_REGISTRATION.name(), user.getLocale(), from, to, data.toString());
//	}
//
//	private EmailRecipient createRecipient(final String name, final String email)
//	{
//		return new EmailRecipient(name, email);
//	}
//
//	private JsonObject createRequestData(final com.transferz.core.auth.infrastructure.UserRegisteredEvent event, final User user)
//	{
//		final JsonObject data = new JsonObject();
//		data.addProperty("password", event.getPassword());
//		data.addProperty("email", user.getEmail());
//		return data;
//	}

}

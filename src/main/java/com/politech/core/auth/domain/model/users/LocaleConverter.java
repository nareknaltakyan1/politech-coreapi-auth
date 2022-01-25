package com.politech.core.auth.domain.model.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.util.Locale;

public class LocaleConverter implements AttributeConverter<Locale, String>
{
	private static Logger LOG = LoggerFactory.getLogger(LocaleConverter.class);

	@Override
	public String convertToDatabaseColumn(final Locale locale)
	{
		if (locale != null)
		{
			final String tag = locale.toLanguageTag();
			LOG.debug("Converting {} to {}", locale, tag);
			return locale.toLanguageTag();
		}
		return null;
	}

	@Override
	public Locale convertToEntityAttribute(final String languageTag)
	{
		if (languageTag != null && !languageTag.isEmpty())
		{
			return Locale.forLanguageTag(languageTag);
		}
		return null;
	}

}

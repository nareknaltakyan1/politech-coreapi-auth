package com.politech.core.auth.domain.model.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum Permission
{
	READ_NON_SENSITIVE(PermissionType.GLOBAL), READ_USERS(PermissionType.GLOBAL), WRITE_USERS(PermissionType.GLOBAL, "READ_USERS", "USER_MANAGER"),
	READ_ORGS(PermissionType.GLOBAL), WRITE_ORGS(PermissionType.GLOBAL, "READ_ORGS"), READ_HUBS(PermissionType.GLOBAL),
	WRITE_HUBS(PermissionType.GLOBAL, "READ_HUBS", "DESTINATION_MANAGER"), READ_QUOTES(PermissionType.GLOBAL),
	WRITE_QUOTES(PermissionType.GLOBAL, "READ_QUOTES"), READ_BOOKINGS(PermissionType.GLOBAL),
	WRITE_BOOKINGS(PermissionType.GLOBAL, "READ_BOOKINGS", "BOOKING_MANAGER", "BOOKING_FINANCE"), READ_TRANSCOS(PermissionType.GLOBAL),
	WRITE_TRANSCOS(PermissionType.GLOBAL, "READ_TRANSCOS", "DESTINATION_MANAGER", "SUPPLY_CURATOR"), READ_PARTNERS(PermissionType.GLOBAL),
	WRITE_PARTNERS(PermissionType.GLOBAL, "READ_PARTNERS", "PARTNER_MANAGER"), READ_INVOICES(PermissionType.GLOBAL),
	WRITE_INVOICES(PermissionType.GLOBAL, "READ_INVOICES", "FINANCE_ADMINISTRATOR"), SEND_EMAIL(PermissionType.GLOBAL),
	VIEW_SENT_EMAIL(PermissionType.GLOBAL), TRIGGER_DATA_SYNC(PermissionType.GLOBAL), MANAGE_DATA_SYNC(PermissionType.GLOBAL, "TRIGGER_DATA_SYNC"),
	GEO_SEARCH(PermissionType.GLOBAL), READ_JOURNEY_OFFERINGS(PermissionType.GLOBAL),
	WRITE_JOURNEY_OFFERINGS(PermissionType.GLOBAL, "READ_JOURNEY_OFFERINGS"), READ_AUDIT_LOGS(PermissionType.GLOBAL),
	WRITE_AUDIT_LOGS(PermissionType.GLOBAL, "READ_AUDIT_LOGS"), IMPERSONATE_USERS(PermissionType.GLOBAL),
	WRITE_PAYMENTS(PermissionType.GLOBAL, "READ_PAYMENTS", "PERFORM_PAYMENTS"), READ_PAYMENTS(PermissionType.GLOBAL),
	PERFORM_PAYMENTS(PermissionType.GLOBAL), CANCEL_JOURNEYS_OUTSIDE_WINDOW(PermissionType.GLOBAL), WRITE_FILES(PermissionType.GLOBAL, "READ_FILES"),
	READ_FILES(PermissionType.GLOBAL), WRITE_CDN_FILES(PermissionType.GLOBAL), CHANGE_JOURNEYS_WITHOUT_PAYMENT(PermissionType.GLOBAL),
	READ_DRIVERS(PermissionType.GLOBAL), WRITE_DRIVERS(PermissionType.GLOBAL, "READ_DRIVERS"),

	CREATE_BOOKINGS(PermissionType.ORGANIZATIONAL, "DEMAND_BOOKER", "DEMAND_MANAGER"),
	VIEW_OWN_BOOKINGS(PermissionType.ORGANIZATIONAL, "SUPPLY_MANAGER", "SUPPLY_PLANNER", "DEMAND_MANAGER"),
	ACCEPT_JOURNEYS(PermissionType.ORGANIZATIONAL, "SUPPLY_MANAGER"),
	PLAN_JOURNEYS(PermissionType.ORGANIZATIONAL, "SUPPLY_MANAGER", "SUPPLY_PLANNER"), PERFORM_JOURNEYS(PermissionType.ORGANIZATIONAL, "DRIVER"),
	READ_ASSIGNED_JOURNEYS(PermissionType.ORGANIZATIONAL), READ_OWN_INVOICES(PermissionType.ORGANIZATIONAL),
	READ_COMPANY_DATA(PermissionType.ORGANIZATIONAL), WRITE_COMPANY_DATA(PermissionType.ORGANIZATIONAL, "READ_COMPANY_DATA");

	private final PermissionType type;
	private final Set<String> aliases;

	Permission(final PermissionType roleType)
	{
		this.type = roleType;
		this.aliases = null;
	}

	Permission(final PermissionType roleType, final String... aliases)
	{
		this.type = roleType;
		final Set<String> tmp = new HashSet<>();
		Collections.addAll(tmp, aliases);
		this.aliases = Collections.unmodifiableSet(tmp);
	}

	public PermissionType getType()
	{
		return type;
	}

	public Set<String> getAliases()
	{
		return aliases;
	}

	public boolean hasAliases()
	{
		return aliases != null;
	}
}

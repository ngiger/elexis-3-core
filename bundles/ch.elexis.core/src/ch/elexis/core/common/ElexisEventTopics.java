package ch.elexis.core.common;

import ch.elexis.core.model.IUser;
import ch.elexis.core.services.IContextService;

public class ElexisEventTopics {

	
	public static final String ECLIPSE_E4_DATA = "org.eclipse.e4.data";
	
	/**
	 * Events generated in the overall elexis context
	 */
	public static final String BASE = "info/elexis/";
	/**
	 * Events generated in the elexis model context
	 */
	public static final String BASE_MODEL = BASE + "model/";
	/**
	 * Events generated in the configuration context
	 */
	public static final String BASE_CONFIG = BASE + "config/";
	/**
	 * Events generated in the elexis JPA persistence context
	 */
	public static final String BASE_JPA = BASE + "jpa/";
	/**
	 * Events generated by PersistentObject entities
	 */
	public static final String BASE_PO = BASE + "po/";
	/**
	 * Events to notify the user
	 */
	public static final String BASE_NOTIFICATION = BASE + "notification/";
	
	public static final String PROPKEY_ID = "id";
	public static final String PROPKEY_CLASS = "class";
	public static final String PROPKEY_USER = "user";
	public static final String PROPKEY_OBJ = "object";

	/**
	 * A persistent object was added, thrown by PersistentObject implementations
	 * only
	 */
	public static final String PERSISTENCE_EVENT_CREATE = BASE_PO + "create";

	/**
	 * Compatibility events posted by the {@link IContextService} on matching events from the
	 * ElexisEventDispatcher. Allows JPA code to refresh entities on PO changes.
	 */
	public static final String PERSISTENCE_EVENT_COMPATIBILITY = BASE_PO + "compatibility/";
	public static final String PERSISTENCE_EVENT_COMPATIBILITY_CREATE =
		PERSISTENCE_EVENT_COMPATIBILITY + "create";
	public static final String PERSISTENCE_EVENT_COMPATIBILITY_DELETE =
		PERSISTENCE_EVENT_COMPATIBILITY + "delete";
	public static final String PERSISTENCE_EVENT_COMPATIBILITY_RELOAD =
		PERSISTENCE_EVENT_COMPATIBILITY + "reload";
	/**
	 * A JPA Entity was changed / saved
	 */
	public static final String PERSISTENCE_EVENT_ENTITYCHANGED = BASE_JPA + "entity/changed";

	/**
	 * Basic model event topics
	 */
	public static final String EVENT_CREATE = BASE_MODEL + "create";
	public static final String EVENT_DELETE = BASE_MODEL + "delete";
	public static final String EVENT_UPDATE = BASE_MODEL + "update";
	public static final String EVENT_RELOAD = BASE_MODEL + "reload";
	/**
	 * Generic reload configuration event
	 */
	public static final String EVENT_CONFIG_RELOAD = BASE_CONFIG + "reload";
	/**
	 * Notify the user
	 */
	public static final String NOTIFICATION_INFO = BASE_NOTIFICATION + "info";
	public static final String NOTIFICATION_ERROR = BASE_NOTIFICATION + "error";
	public static final String NOTIFICATION_WARN = BASE_NOTIFICATION + "warn";
	public static final String NOTIFICATION_PROPKEY_TITLE = "title";
	public static final String NOTIFICATION_PROPKEY_MESSAGE = "message";

	/**
	 * User was changed (e.g. via login or re-login), delivers {@link IUser} or <code>null</code> on
	 * logout. (Re-Login events: <code>null</code> and the new user). Please consider direct e4 injection
	 * see https://redmine.medelexis.ch/issues/19669
	 */
	public static final String EVENT_USER_CHANGED = BASE + "user/changed";
	
	/**
	 * Topics concerning locking object events
	 */
	public static final String LOCKING_EVENT = BASE + "locking/";

	public static final String EVENT_LOCK_AQUIRED = LOCKING_EVENT + "aquired";
	public static final String EVENT_LOCK_RELEASED = LOCKING_EVENT + "released";
	public static final String EVENT_LOCK_PRERELEASE = LOCKING_EVENT + "prerelease";

	/**
	 * Topics concerning stock commissioning systems
	 */
	public static final String BASE_STOCK_COMMISSIONING = BASE + "stockCommissioning/";
	public static final String STOCK_COMMISSIONING_OUTLAY = BASE_STOCK_COMMISSIONING + "outlay";
	public static final String STOCK_COMMISSIONING_PROPKEY_STOCKENTRY_ID = "stockEntryId";
	public static final String STOCK_COMMISSIONING_PROPKEY_QUANTITY = "quantity";

	/**
	 * Perform a stock count (inventory) of the articles in the respective stock, or
	 * all if {@link #STOCK_COMMISSIONING_PROPKEY_LIST_ARTICLE_ID} not provided
	 */
	public static final String STOCK_COMMISSIONING_SYNC_STOCK = BASE_STOCK_COMMISSIONING + "updateStock";
	/**
	 * List<String> of article identifiers
	 */
	public static final String STOCK_COMMISSIONING_PROPKEY_LIST_ARTICLE_ID = "articleIds";
	/**
	 * The ID of the stock the request is targeted to
	 */
	public static final String STOCK_COMMISSIONING_PROPKEY_STOCK_ID = "stockId";
}

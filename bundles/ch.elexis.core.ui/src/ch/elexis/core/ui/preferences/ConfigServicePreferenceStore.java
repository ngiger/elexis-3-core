package ch.elexis.core.ui.preferences;

import static ch.elexis.core.constants.Preferences.SETTINGS_PREFERENCE_STORE_DEFAULT;

import javax.inject.Inject;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import ch.elexis.core.model.IContact;
import ch.elexis.core.model.IMandator;
import ch.elexis.core.services.IConfigService;
import ch.elexis.core.services.IContextService;
import ch.elexis.core.ui.util.CoreUiUtil;

/**
 * Adapter for {@link IConfigService} to {@link IPreferenceStore}. Replacement for
 * {@link ch.elexis.core.ui.preferences.SettingsPreferenceStore}.
 * 
 * @author thomas
 *
 */
public class ConfigServicePreferenceStore extends EventManager implements IPreferenceStore {
	
	public enum Scope {
			GLOBAL, USER, MANDATOR, LOCAL
	}
	
	@Inject
	private IConfigService configService;
	
	@Inject
	private IContextService contextService;
	
	private Scope scope;
	
	private ListenerList<IPropertyChangeListener> listeners;
	
	/**
	 * Create a new instance of the receiver. Store the values in context to the {@link Scope} using
	 * {@link IConfigService}.
	 * 
	 * @param scope
	 *            the scope to store to
	 */
	public ConfigServicePreferenceStore(Scope scope){
		this.scope = scope;
		CoreUiUtil.injectServices(this);
		listeners = new ListenerList<>();
	}
	
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener){
		listeners.add(listener);
	}
	
	@Override
	public boolean contains(String name){
		if (name != null) {
			return getStringValue(name) != null;
		}
		return false;
	}
	
	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue){
		for (IPropertyChangeListener l : listeners) {
			l.propertyChange(new PropertyChangeEvent(this, name, oldValue, newValue));
		}
	}
	
	@Override
	public boolean getBoolean(String name){
		if (scope == Scope.GLOBAL) {
			return configService.get(name, BOOLEAN_DEFAULT_DEFAULT);
		} else if (scope == Scope.MANDATOR) {
			IMandator activeMandator = contextService.getActiveMandator()
				.orElseThrow(() -> new IllegalStateException("No active mandator"));
			return configService.get(activeMandator, name, BOOLEAN_DEFAULT_DEFAULT);
		} else if (scope == Scope.USER) {
			IContact activeUser = contextService.getActiveUserContact()
				.orElseThrow(() -> new IllegalStateException("No active user contact"));
			return configService.get(activeUser, name, BOOLEAN_DEFAULT_DEFAULT);
		} else if (scope == Scope.LOCAL) {
			return configService.getLocal(name, BOOLEAN_DEFAULT_DEFAULT);
		} else {
			throw new IllegalStateException("Unknown scope " + scope);
		}
	}
	

	@Override
	public boolean getDefaultBoolean(String name){
		String value = getDefaultStringValue(name);
		if (value != null) {
			return getBoolean(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
		}
		return BOOLEAN_DEFAULT_DEFAULT;
	}
	
	@Override
	public double getDefaultDouble(String name){
		String value = getDefaultStringValue(name);
		if (value != null) {
			return getDouble(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
		}
		return DOUBLE_DEFAULT_DEFAULT;
	}
	
	@Override
	public float getDefaultFloat(String name){
		String value = getDefaultStringValue(name);
		if (value != null) {
			return getFloat(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
		}
		return FLOAT_DEFAULT_DEFAULT;
	}
	
	@Override
	public int getDefaultInt(String name){
		String value = getDefaultStringValue(name);
		if (value != null) {
			return getInt(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
		}
		return INT_DEFAULT_DEFAULT;
	}
	
	@Override
	public long getDefaultLong(String name){
		String value = getDefaultStringValue(name);
		if (value != null) {
			return getLong(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
		}
		return LONG_DEFAULT_DEFAULT;
	}
	
	@Override
	public String getDefaultString(String name){
		String value = getDefaultStringValue(name);
		if (value != null) {
			return getString(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
		}
		return STRING_DEFAULT_DEFAULT;
	}
	
	private String getDefaultStringValue(String name){
		return getStringValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT);
	}
	
	private String getStringValue(String name){
		String value = null;
		if (scope == Scope.GLOBAL) {
			value = configService.get(name, null);
		} else if (scope == Scope.MANDATOR) {
			IMandator activeMandator = contextService.getActiveMandator()
				.orElseThrow(() -> new IllegalStateException("No active mandator"));
			value = configService.get(activeMandator, name, null);
		} else if (scope == Scope.USER) {
			IContact activeUser = contextService.getActiveUserContact()
				.orElseThrow(() -> new IllegalStateException("No active user contact"));
			value = configService.get(activeUser, name, null);
		} else if (scope == Scope.LOCAL) {
			value = configService.getLocal(name, null);
		} else {
			throw new IllegalStateException("Unknown scope " + scope);
		}
		return value;
	}
	
	private void setStringValue(String name, String value){
		if (scope == Scope.GLOBAL) {
			configService.set(name, value);
		} else if (scope == Scope.MANDATOR) {
			IMandator activeMandator = contextService.getActiveMandator()
				.orElseThrow(() -> new IllegalStateException("No active mandator"));
			configService.set(activeMandator, name, value);
		} else if (scope == Scope.USER) {
			IContact activeUser = contextService.getActiveUserContact()
				.orElseThrow(() -> new IllegalStateException("No active user contact"));
			configService.set(activeUser, name, value);
		} else if (scope == Scope.LOCAL) {
			configService.setLocal(name, value);
		} else {
			throw new IllegalStateException("Unknown scope " + scope);
		}
	}
	
	@Override
	public double getDouble(String name){
		String value = getStringValue(name);
		if (value == null) {
			return DOUBLE_DEFAULT_DEFAULT;
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return DOUBLE_DEFAULT_DEFAULT;
		}
	}
	
	@Override
	public float getFloat(String name){
		String value = getStringValue(name);
		if (value == null) {
			return FLOAT_DEFAULT_DEFAULT;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return FLOAT_DEFAULT_DEFAULT;
		}
	}
	
	@Override
	public int getInt(String name){
		if (scope == Scope.GLOBAL) {
			return configService.get(name, INT_DEFAULT_DEFAULT);
		} else if (scope == Scope.MANDATOR) {
			IMandator activeMandator = contextService.getActiveMandator()
				.orElseThrow(() -> new IllegalStateException("No active mandator"));
			return configService.get(activeMandator, name, INT_DEFAULT_DEFAULT);
		} else if (scope == Scope.USER) {
			IContact activeUser = contextService.getActiveUserContact()
				.orElseThrow(() -> new IllegalStateException("No active user contact"));
			return configService.get(activeUser, name, INT_DEFAULT_DEFAULT);
		} else if (scope == Scope.LOCAL) {
			return configService.getLocal(name, INT_DEFAULT_DEFAULT);
		} else {
			throw new IllegalStateException("Unknown scope " + scope);
		}
	}
	
	@Override
	public long getLong(String name){
		String value = getStringValue(name);
		if (value == null) {
			return LONG_DEFAULT_DEFAULT;
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return LONG_DEFAULT_DEFAULT;
		}
	}
	
	@Override
	public String getString(String name){
		String value = getStringValue(name);
		return value == null ? STRING_DEFAULT_DEFAULT : value;
	}
	
	@Override
	public boolean isDefault(String name){
		if (name == null) {
			return false;
		}
		return getStringValue(name) == null;
	}
	
	@Override
	public boolean needsSaving(){
		// save is immediately done be IConfigService
		return false;
	}
	
	@Override
	public void putValue(String name, String value){
		setStringValue(name, value);
	}
	
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public void setDefault(String name, double value){
		setValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT, value);
	}
	
	@Override
	public void setDefault(String name, float value){
		setValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT, value);
	}
	
	@Override
	public void setDefault(String name, int value){
		setValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT, value);
	}
	
	@Override
	public void setDefault(String name, long value){
		setValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT, value);
	}
	
	@Override
	public void setDefault(String name, String value){
		setValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT, value);
	}
	
	@Override
	public void setDefault(String name, boolean value){
		setValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT, value);
	}
	
	@Override
	public void setToDefault(String name){
		setStringValue(name, getStringValue(name + SETTINGS_PREFERENCE_STORE_DEFAULT));
	}
	
	@Override
	public void setValue(String name, double value){
		double oldValue = getDouble(name);
		if (oldValue == value) {
			return;
		}
		firePropertyChangeEvent(name, Double.valueOf(oldValue), Double.valueOf(value));
		setStringValue(name, Double.toString(value));
	}
	
	@Override
	public void setValue(String name, float value){
		float oldValue = getFloat(name);
		if (oldValue == value) {
			return;
		}
		firePropertyChangeEvent(name, Float.valueOf(oldValue), Float.valueOf(value));
		setStringValue(name, Float.toString(value));
	}
	
	@Override
	public void setValue(String name, int value){
		int oldValue = getInt(name);
		if (oldValue == value) {
			return;
		}
		firePropertyChangeEvent(name, oldValue, value);
		if (scope == Scope.GLOBAL) {
			configService.set(name, value);
		} else if (scope == Scope.MANDATOR) {
			IMandator activeMandator = contextService.getActiveMandator()
				.orElseThrow(() -> new IllegalStateException("No active mandator"));
			configService.set(activeMandator, name, value);
		} else if (scope == Scope.USER) {
			IContact activeUser = contextService.getActiveUserContact()
				.orElseThrow(() -> new IllegalStateException("No active user contact"));
			configService.set(activeUser, name, value);
		} else if (scope == Scope.LOCAL) {
			configService.setLocal(name, value);
		} else {
			throw new IllegalStateException("Unknown scope " + scope);
		}
	}
	
	@Override
	public void setValue(String name, long value){
		long oldValue = getLong(name);
		if (oldValue == value) {
			return;
		}
		firePropertyChangeEvent(name, Long.valueOf(oldValue), Long.valueOf(value));
		setStringValue(name, Long.toString(value));
	}
	
	@Override
	public void setValue(String name, String value){
		String oldValue = getString(name);
		if (oldValue.equals(value)) {
			return;
		}
		firePropertyChangeEvent(name, oldValue, value);
		setStringValue(name, value);
	}
	
	@Override
	public void setValue(String name, boolean value){
		boolean oldValue = getBoolean(name);
		if (oldValue == value) {
			return;
		}
		firePropertyChangeEvent(name, oldValue ? Boolean.TRUE : Boolean.FALSE,
				value ? Boolean.TRUE : Boolean.FALSE);
		if (scope == Scope.GLOBAL) {
			configService.set(name, value);
		} else if (scope == Scope.MANDATOR) {
			IMandator activeMandator = contextService.getActiveMandator()
				.orElseThrow(() -> new IllegalStateException("No active mandator"));
			configService.set(activeMandator, name, value);
		} else if (scope == Scope.USER) {
			IContact activeUser = contextService.getActiveUserContact()
				.orElseThrow(() -> new IllegalStateException("No active user contact"));
			configService.set(activeUser, name, value);
		} else {
			throw new IllegalStateException("Unknown scope " + scope);
		}
	}
}

package ch.elexis.core.ui.events;

import org.eclipse.core.runtime.IStatus;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.LoggerFactory;

import ch.elexis.core.common.ElexisEvent;
import ch.elexis.core.common.ElexisEventTopics;
import ch.elexis.core.services.IElexisServerService;
import ch.elexis.core.status.StatusUtil;

/**
 * Forward create events to the server (currently used by rockethealth to pick
 * these up)
 */
@Component(property = { EventConstants.EVENT_TOPIC + "=" + ElexisEventTopics.PERSISTENCE_EVENT_CREATE })
public class ElexisServerCreateEventForwarderEventHandler implements EventHandler {

	@Reference
	private IElexisServerService elexisServerService;

	@Override
	public void handleEvent(Event event) {
		if (elexisServerService.deliversRemoteEvents()) {
			ch.elexis.core.common.ElexisEvent mapEvent = mapEvent(event);
			if (mapEvent != null) {
				IStatus status = elexisServerService.postEvent(mapEvent);
				if (!status.isOK()) {
					StatusUtil.printStatus(LoggerFactory.getLogger(getClass()), status);
				}
			}
		}
	}

	private ElexisEvent mapEvent(Event event) {
		ch.elexis.core.common.ElexisEvent remoteEvent = new ch.elexis.core.common.ElexisEvent();
		remoteEvent.setTopic(event.getTopic());
		String[] propertyNames = event.getPropertyNames();
		for (String name : propertyNames) {
			String value = event.getProperty(name) != null ? event.getProperty(name).toString() : null;
			remoteEvent.putProperty(name, value);
		}
		return remoteEvent;
	}

}

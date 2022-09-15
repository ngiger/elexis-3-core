package ch.elexis.core.importer.div.tasks.internal;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.ComponentException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

import ch.elexis.core.importer.div.importers.ILabImportUtil;
import ch.elexis.core.importer.div.tasks.BillLabResultOnCreationIdentifiedRunnable;
import ch.elexis.core.importer.div.tasks.BillLabResultOnCreationTemplateTaskDescriptor;
import ch.elexis.core.model.tasks.IIdentifiedRunnable;
import ch.elexis.core.model.tasks.IIdentifiedRunnableFactory;
import ch.elexis.core.model.tasks.TaskException;
import ch.elexis.core.services.IModelService;
import ch.elexis.core.services.IVirtualFilesystemService;
import ch.elexis.core.tasks.model.ITaskService;

@Component(immediate = true)
public class HL7ImporterIdentifiedRunnableFactory implements IIdentifiedRunnableFactory {

	@Reference
	private ITaskService taskService;

	private IModelService coreModelService;

	@Reference(target = "(" + IModelService.SERVICEMODELNAME + "=ch.elexis.core.model)")
	private void setModelService(IModelService modelService) {
		coreModelService = modelService;
	}

	@Reference
	private ILabImportUtil labimportUtil;

	@Reference
	private IVirtualFilesystemService vfsService;

	@Activate
	private void activate() {
		try {
			HL7ImporterTemplateTaskDescriptor.assertTemplate(taskService);
			BillLabResultOnCreationTemplateTaskDescriptor.assertTemplate(taskService);
		} catch (TaskException e) {
			LoggerFactory.getLogger(getClass()).error("initialize", e);
			throw new ComponentException(e);
		}
		taskService.bindIIdentifiedRunnableFactory(this);
	}

	@Deactivate
	public void deactivate() {
		taskService.unbindIIdentifiedRunnableFactory(this);
	}

	@Override
	public List<IIdentifiedRunnable> getProvidedRunnables() {
		List<IIdentifiedRunnable> ret = new ArrayList<>();
		ret.add(new HL7ImporterIIdentifiedRunnable(coreModelService, labimportUtil, vfsService));
		ret.add(new BillLabResultOnCreationIdentifiedRunnable(coreModelService, null));
		return ret;
	}

}

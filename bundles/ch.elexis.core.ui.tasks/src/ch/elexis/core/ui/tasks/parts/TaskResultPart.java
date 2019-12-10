package ch.elexis.core.ui.tasks.parts;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.elexis.core.common.ElexisEventTopics;
import ch.elexis.core.services.IQuery;
import ch.elexis.core.tasks.model.ITask;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.core.ui.tasks.internal.TaskModelServiceHolder;

public class TaskResultPart implements IDoubleClickListener {
	
	@Inject
	private ESelectionService selectionService;
	
	@Inject
	private EPartService partService;
	
	private Composite tableViewerComposite;
	private Table tableResults;
	private TableViewer tableViewerResults;
	
	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService){
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		tableViewerComposite = new Composite(parent, SWT.NONE);
		tableViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableColumnLayout tcLayout = new TableColumnLayout();
		tableViewerComposite.setLayout(tcLayout);
		
		tableViewerResults = new TableViewer(tableViewerComposite,
			SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI);
		tableResults = tableViewerResults.getTable();
		tableResults.setHeaderVisible(true);
		tableResults.setLinesVisible(true);
		LazyTaskResultContentProvider contentProvider =
			new LazyTaskResultContentProvider(tableViewerResults);
		tableViewerResults.setContentProvider(contentProvider);
		tableViewerResults.setUseHashlookup(true);
		tableViewerResults.addDoubleClickListener(this);
		
		TableViewerColumn tvcState = new TableViewerColumn(tableViewerResults, SWT.NONE);
		tvcState.setLabelProvider(TaskResultLabelProvider.getInstance());
		TableColumn tblclmnState = tvcState.getColumn();
		tcLayout.setColumnData(tblclmnState, new ColumnPixelData(16, true, true));
		tblclmnState.setText("Status");
		
		TableViewerColumn tvcLastUpdate = new TableViewerColumn(tableViewerResults, SWT.NONE);
		tvcLastUpdate.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				ITask task = (ITask) element;
				LocalDateTime lastUpdateDt = LocalDateTime
					.ofInstant(Instant.ofEpochMilli(task.getLastupdate()), ZoneId.systemDefault());
				return lastUpdateDt.toString();
			}
		});
		TableColumn tblclmnLastupdate = tvcLastUpdate.getColumn();
		tcLayout.setColumnData(tblclmnLastupdate, new ColumnPixelData(150, true, true));
		tblclmnLastupdate.setText("Aktualisiert");
		
		TableViewerColumn tvcOwner = new TableViewerColumn(tableViewerResults, SWT.NONE);
		tvcOwner.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				ITask task = (ITask) element;
				return task.getTaskDescriptor().getOwner().getId();
			}
		});
		TableColumn tblclmnOwner = tvcOwner.getColumn();
		tcLayout.setColumnData(tblclmnOwner, new ColumnPixelData(100, true, true));
		tblclmnOwner.setText("User");
		
		TableViewerColumn tvcTrigger = new TableViewerColumn(tableViewerResults, SWT.NONE);
		tvcTrigger.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element){
				ITask task = (ITask) element;
				switch (task.getTriggerEvent()) {
				case MANUAL:
					return Images.IMG_HAND.getImage();
				case CRON:
					return Images.IMG_CLOCK.getImage();
				case SYSTEM_EVENT:
					return Images.IMG_SYSTEM_MONITOR.getImage();
				case OTHER_TASK:
					return Images.IMG_TASK.getImage();
				default:
					break;
				}
				return super.getImage(element);
			}
			
			@Override
			public String getText(Object element){
				return null;
			}
		});
		TableColumn tblclmnTrigger = tvcTrigger.getColumn();
		tcLayout.setColumnData(tblclmnTrigger, new ColumnPixelData(16, true, true));
		tblclmnTrigger.setText("Auslöser");
		
		TableViewerColumn tvcTaskDescriptor = new TableViewerColumn(tableViewerResults, SWT.NONE);
		tvcTaskDescriptor.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				ITask task = (ITask) element;
				String referenceId = task.getTaskDescriptor().getReferenceId();
				if (referenceId != null) {
					return referenceId;
				}
				return task.getTaskDescriptor().getId();
			}
		});
		TableColumn tblclmnTaskDescriptor = tvcTaskDescriptor.getColumn();
		tcLayout.setColumnData(tblclmnTaskDescriptor, new ColumnPixelData(100, true, true));
		tblclmnTaskDescriptor.setText("Vorlage");
		
		tableViewerResults.addSelectionChangedListener(event -> {
			IStructuredSelection selection = tableViewerResults.getStructuredSelection();
			selectionService.setSelection(selection.toList());
		});
		
		menuService.registerContextMenu(tableResults,
			"ch.elexis.core.ui.tasks.popupmenu.tableresults");
		
		refresh();
	}
	
	public void refresh(){
		IQuery<ITask> taskQuery = TaskModelServiceHolder.get().getQuery(ITask.class);
		List<ITask> results = taskQuery.execute();
		// add filter
		tableViewerResults.setInput(results);
		tableViewerResults.setItemCount(results.size());
	}
	
	@Focus
	public void setFocus(){
		tableResults.setFocus();
	}
	
	@Optional
	@Inject
	void deleteTask(@UIEventTopic(ElexisEventTopics.EVENT_DELETE) ITask iTask){
		tableViewerResults.remove(iTask);
	}
	
	@Override
	public void doubleClick(DoubleClickEvent event){
		ITask selectedTask = (ITask) ((StructuredSelection) event.getSelection()).getFirstElement();
		MPart taskDetailPart =
			partService.createPart("ch.elexis.core.ui.tasks.partdescriptor.taskdetail");
		taskDetailPart.getTransientData().put("task", selectedTask);
		partService.showPart(taskDetailPart, PartState.CREATE);
	}
}
/*******************************************************************************
 * Copyright (c) 2005-2011, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *******************************************************************************/

package ch.elexis.core.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.services.ISourceProviderService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.core.common.ElexisEventTopics;
import ch.elexis.core.constants.Elexis;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.data.interfaces.ShutdownJob;
import ch.elexis.core.data.interfaces.scripting.Interpreter;
import ch.elexis.core.data.util.NoPoUtil;
import ch.elexis.core.l10n.Messages;
import ch.elexis.core.model.IPatient;
import ch.elexis.core.model.IUser;
import ch.elexis.core.ui.actions.GlobalActions;
import ch.elexis.core.ui.commands.sourceprovider.PatientSelectionStatus;
import ch.elexis.core.ui.dialogs.ReminderListSelectionDialog;
import ch.elexis.core.ui.e4.util.CoreUiUtil;
import ch.elexis.core.ui.locks.ToggleCurrentPatientLockHandler;
import ch.elexis.core.ui.preferences.PreferenceInitializer;
import ch.elexis.data.Anwender;
import ch.elexis.data.Mandant;
import ch.elexis.data.Patient;
import ch.elexis.data.Reminder;
import ch.elexis.data.VerrechenbarFavorites;
import ch.rgw.tools.TimeTool;

/**
 * Diese Klasse ist der OSGi-Activator und steuert somit Start und Ende der
 * Anwendung. Ganz früh (vor dem Initialisieren der anwendung) und ganz spät
 * (unmittelbar vor dem Entfernen der Anwendung) notwendige Schritte müssen hier
 * durchgeführt werden. Ausserdem werden hier globale Variablen und Konstanten
 * angelegt.
 *
 * @since 3.0.0 major rewrites
 */
public class Hub extends AbstractUIPlugin {
	// Globale Konstanten
	public static final String APPLICATION_NAME = "Elexis"; //$NON-NLS-1$
	public static final String PLUGIN_ID = "ch.elexis.core.ui"; //$NON-NLS-1$
	public static final String COMMAND_PREFIX = PLUGIN_ID + ".commands."; //$NON-NLS-1$
	public static final String SWTBOTTEST_KEY = "ch.elexis.swtbottest.key"; //$NON-NLS-1$
	static final String[] mine = { "ch.elexis", "ch.rgw" }; //$NON-NLS-1$ //$NON-NLS-2$

	private static Logger log = LoggerFactory.getLogger(Hub.class.getName());

	private static List<ShutdownJob> shutdownJobs = new LinkedList<ShutdownJob>();

	/** Der Initialisierer für die Voreinstellungen */
	public static final PreferenceInitializer pin = new PreferenceInitializer();

	// Globale Variable
	/**
	 * Suche externe Config - poor mans dependency -> see
	 * ch.elexis.ElexisConfigurationConstants.java
	 */
	public static boolean ecc = ElexisConfigurationConstants.init();

	/** Das Singleton-Objekt dieser Klasse */
	public static Hub plugin;

	/** Globale Aktionen */
	public static GlobalActions mainActions;

	@Inject
	private ISourceProviderService sps;

	@Inject
	private ICommandService commandService;

	@Inject
	public void activePatient(@Optional IPatient patient) {
		Patient pat = (Patient) NoPoUtil.loadAsPersistentObject(patient);
		Hub.setWindowText(pat);

		commandService.refreshElements(ToggleCurrentPatientLockHandler.COMMAND_ID, null);

		PatientSelectionStatus provider = (PatientSelectionStatus) sps
				.getSourceProvider(PatientSelectionStatus.PATIENTACTIVE);
		if (provider == null) {
			return;
		}

		provider.setState(pat != null);
	}

	@Optional
	@Inject
	void activeUser(IUser user) {
		updateUser(user);
	}

	@Optional
	@Inject
	void changedUser(@EventTopic(ElexisEventTopics.EVENT_USER_CHANGED) IUser user) {
		updateUser(user);
	}

	private void updateUser(IUser user) {
		// reminder
		if (CoreHub.getLoggedInContact() != null) {
			Anwender loggedInContact = CoreHub.getLoggedInContact();
			CompletableFuture.runAsync(() -> {
				final List<Reminder> reminderList = Reminder.findToShowOnStartup(loggedInContact);

				if (reminderList.size() > 0) {
					// must be called inside display thread
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							new ReminderListSelectionDialog(reminderList,
									Messages.ReminderView_importantRemindersOnLogin).open();
						}
					});
				}
			});
		}
		// favorites
		VerrechenbarFavorites.reset();
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		log.debug("Starting " + this.getClass().getName()); //$NON-NLS-1$
		plugin = this;

		CoreUiUtil.injectServicesWithContext(this);

		// add UI ClassLoader to default Script Interpreter
		Interpreter.classLoaders.add(Hub.class.getClassLoader());
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		log.debug("Stopping " + this.getClass().getName()); //$NON-NLS-1$

		super.stop(context);
	}

	/**
	 * Programmende
	 */
	public static void postShutdown() {
		// shutdownjobs are executed after the workbench has been shut down.
		// So those jobs must not use any of the workbench's resources.
		if ((shutdownJobs != null) && (shutdownJobs.size() > 0)) {
			Shell shell = new Shell(Display.getDefault());
			MessageDialog dlg = new MessageDialog(shell, Messages.Hub_title_configuration, Dialog.getDefaultImage(),
					Messages.Hub_message_configuration, SWT.ICON_INFORMATION, new String[] {}, 0);
			dlg.setBlockOnOpen(false);
			dlg.open();
			for (ShutdownJob job : shutdownJobs) {
				try {
					job.doit();
				} catch (Exception e) {
					log.error("Error starting job: " + e.getMessage()); //$NON-NLS-1$
				}
			}
			dlg.close();
		}
	}

	public static void setMandant(final Mandant m) {
		CoreHub.setMandant(m);
		setWindowText(null);
	}

	/*
	 * Sets the window title to a nicely formatted string containt the family name,
	 * name, age and its code
	 */
	public static void setWindowText(Patient pat) {
		LoggerFactory.getLogger(Hub.class).info("Set patient [" + pat + "]");
		StringBuilder sb = new StringBuilder();
		sb.append("Elexis ").append(Elexis.VERSION).append(" - "); //$NON-NLS-1$ //$NON-NLS-2$
		if (CoreHub.getLoggedInContact() == null) {
			sb.append(Messages.Hub_nouserloggedin);
		} else {
			sb.append(StringUtils.SPACE).append(CoreHub.getLoggedInContact().getLabel());
		}
		if (CoreHub.actMandant == null) {
			sb.append(Messages.Hub_nomandantor);

		} else {
			sb.append(" / ").append(CoreHub.actMandant.getLabel()); //$NON-NLS-1$
		}
		if (pat == null) {
			pat = (Patient) ElexisEventDispatcher.getSelected(Patient.class);
		}
		if (pat == null) {
			sb.append(Messages.Hub_nopatientselected);
		} else {
			String nr = pat.getPatCode();
			String alter = pat.getAlter();
			sb.append("  / ").append(pat.getLabel()).append(" (").append(alter).append(") - ") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					.append("[").append(nr).append("]"); //$NON-NLS-1$ //$NON-NLS-2$

			if (Reminder.findForPatient(pat, CoreHub.getLoggedInContact()).size() != 0) {
				sb.append(Messages.Hub_message_reminders);
			}
			String act = new TimeTool().toString(TimeTool.DATE_COMPACT);
			TimeTool ttPatg = new TimeTool();
			if (ttPatg.set(pat.getGeburtsdatum())) {
				String patg = ttPatg.toString(TimeTool.DATE_COMPACT);
				if (act.substring(4).equals(patg.substring(4))) {
					sb.append(Messages.Hub_message_birthday);
				}
			}
		}
		if (mainActions.mainWindow != null) {
			Shell shell = mainActions.mainWindow.getShell();
			if ((shell != null) && (!shell.isDisposed())) {
				mainActions.mainWindow.getShell().setText(sb.toString());
			}
		}
	}

	/**
	 * get the currently active Shell. If no such Shell exists, it will be created
	 * using dhe default Display.
	 *
	 * @return always a valid shell. Never null
	 */
	public static Shell getActiveShell() {
		if (plugin != null) {
			IWorkbench wb = plugin.getWorkbench();
			if (wb != null) {
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				if (win != null) {
					return win.getShell();
				}
			}
		}
		Display dis = UiDesk.getDisplay();
		if (dis == null) {
			dis = PlatformUI.createDisplay();
		}
		return new Shell(dis);
	}

	public static List<Mandant> getMandantenList() {
		return CoreHub.getMandantenList();
	}

	public static List<Anwender> getUserList() {
		return CoreHub.getUserList();
	}

	/**
	 * Add a ShutdownJob to the list of jobs that has to be done after the Elexis
	 * workbench was shut down.
	 *
	 * @param job
	 * @deprecated Use <code>PlatformUI.getWorkbench().addWorkbenchListener()</code>
	 */
	public static void addShutdownJob(final ShutdownJob job) {
		if (!shutdownJobs.contains(job)) {
			shutdownJobs.add(job);
		}
	}
}

package ch.elexis.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.elexis.admin.RoleBasedAccessControlTest;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.JdbcLinkException;

@RunWith(Suite.class)
@SuiteClasses({
	Test_DBInitialState.class, Test_PersistentObject.class, Test_Prescription.class,
	Test_Patient.class, Test_LabItem.class, Test_DBImage.class, Test_Query.class,
	Test_Verrechnet.class, Test_Reminder.class, Test_StockService.class, Test_OrderService.class,
	Test_Konsultation.class, RoleBasedAccessControlTest.class
})
public class AllDataTests {
	
	private static Collection<Object[]> connections = new ArrayList<Object[]>();
	
	public static final boolean PERFORM_UPDATE_TESTS = false;
	
	static {
		JdbcLink h2JdbcLink = TestInitializer.initTestDBConnection(TestInitializer.FLAVOR_H2);
		JdbcLink mySQLJdbcLink = TestInitializer.initTestDBConnection(TestInitializer.FLAVOR_MYSQL);
		JdbcLink pgJdbcLink = TestInitializer.initTestDBConnection(TestInitializer.FLAVOR_POSTGRES);
		
		assertNotNull(h2JdbcLink);
		AllDataTests.connections.add(new Object[] {
			h2JdbcLink
		});
		if (mySQLJdbcLink != null) {
			AllDataTests.connections.add(new Object[] {
				mySQLJdbcLink
			});
		}
		if (pgJdbcLink != null) {
			AllDataTests.connections.add(new Object[] {
				pgJdbcLink
			});
		}
	}
	
	@AfterClass
	public static void afterClass(){
		for (Object[] objects : AllDataTests.connections) {
			JdbcLink link = (JdbcLink) objects[0];
			try {
				PersistentObject.connect(link);
				PersistentObject.deleteAllTables();
				link.disconnect();
			} catch (JdbcLinkException je) {
				// just tell what happened and resume
				// exception is allowed for tests which get rid of the connection on their own
				// for example testConnect(), ...
				je.printStackTrace();
			}
		}
	}
	
	public static Collection<Object[]> getConnections() throws IOException{
		assertTrue(connections.size() > 0);
		return connections;
	}
}
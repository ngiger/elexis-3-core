package ch.elexis.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.elexis.core.services.IQuery;
import ch.elexis.core.services.IQuery.COMPARATOR;

public class DocumentBriefTest extends AbstractTest {
	
	private IContact contact1;
	private IPatient patient1;
	
	@Before
	public void before(){
		super.before();
		contact1 = modelService.create(IContact.class);
		contact1.setDescription1("test contact 1");
		modelService.save(contact1);
		patient1 = modelService.create(IPatient.class);
		patient1.setDescription1("test patient 2");
		modelService.save(patient1);
	}
	
	@After
	public void after(){
		modelService.remove(contact1);
		modelService.remove(patient1);
		super.after();
	}
	
	@Test
	public void create() throws IOException{
		IDocumentLetter letter = modelService.create(IDocumentLetter.class);
		assertNotNull(letter);
		assertTrue(letter instanceof IDocumentLetter);
		
		letter.setDescription("test letter 1");
		letter.setAuthor(contact1);
		letter.setContent(new ByteArrayInputStream("test content".getBytes()));
		assertTrue(modelService.save(letter));
		
		Optional<IDocumentLetter> loadedLetter =
			modelService.load(letter.getId(), IDocumentLetter.class);
		assertTrue(loadedLetter.isPresent());
		assertFalse(letter == loadedLetter.get());
		assertEquals(letter, loadedLetter.get());
		assertEquals(letter.getDescription(), loadedLetter.get().getDescription());
		try (ByteArrayOutputStream contentByteArray = new ByteArrayOutputStream();
				InputStream contentStream = letter.getContent()) {
			IOUtils.copy(contentStream, contentByteArray);
			assertEquals("test content", new String(contentByteArray.toByteArray()));
		}
		modelService.remove(letter);
	}
	
	@Test
	public void query() throws IOException{
		IDocumentLetter letter1 = modelService.create(IDocumentLetter.class);
		letter1.setDescription("test letter 1");
		letter1.setAuthor(contact1);
		letter1.setContent(new ByteArrayInputStream("test content 1".getBytes()));
		assertTrue(modelService.save(letter1));
		IDocumentLetter letter2 = modelService.create(IDocumentLetter.class);
		letter2.setDescription("test letter 2");
		letter2.setAuthor(contact1);
		letter2.setPatient(patient1);
		letter2.setContent(new ByteArrayInputStream("test content 2".getBytes()));
		assertTrue(modelService.save(letter2));
		
		IQuery<IDocumentLetter> query = modelService.getQuery(IDocumentLetter.class);
		query.and(ModelPackage.Literals.IDOCUMENT__PATIENT, COMPARATOR.EQUALS, patient1);
		List<IDocumentLetter> existing = query.execute();
		assertNotNull(existing);
		assertFalse(existing.isEmpty());
		assertFalse(letter2 == existing.get(0));
		assertEquals(letter2, existing.get(0));
		assertEquals(letter2.getDescription(), existing.get(0).getDescription());
		IOUtils.contentEquals(letter2.getContent(), existing.get(0).getContent());
		
		modelService.remove(letter1);
		modelService.remove(letter2);
	}
}

package ch.elexis.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.elexis.core.jpa.entities.Kontakt;
import ch.elexis.core.jpa.entities.KontaktAdressJoint;
import ch.elexis.core.jpa.entities.ZusatzAdresse;
import ch.elexis.core.jpa.model.adapter.AbstractIdDeleteModelAdapter;
import ch.elexis.core.jpa.model.adapter.AbstractIdModelAdapter;
import ch.elexis.core.jpa.model.adapter.mixin.ExtInfoHandler;
import ch.elexis.core.jpa.model.adapter.mixin.IdentifiableWithXid;
import ch.elexis.core.model.service.holder.CoreModelServiceHolder;
import ch.elexis.core.model.util.internal.ModelUtil;
import ch.elexis.core.types.Country;

public class Contact extends AbstractIdDeleteModelAdapter<Kontakt> implements IdentifiableWithXid, IContact {

	private ExtInfoHandler extInfoHandler;

	public Contact(Kontakt entity) {
		super(entity);
		extInfoHandler = new ExtInfoHandler(this);
	}

	@Override
	public boolean isMandator() {
		return getEntity().isMandator();
	}

	@Override
	public void setMandator(boolean value) {
		getEntity().setMandator(value);
	}

	@Override
	public boolean isUser() {
		return getEntity().isUser();
	}

	@Override
	public void setUser(boolean value) {
		getEntity().setUser(value);
	}

	@Override
	public boolean isPerson() {
		return getEntity().isPerson();
	}

	@Override
	public void setPerson(boolean value) {
		getEntity().setPerson(value);
	}

	@Override
	public boolean isPatient() {
		return getEntity().isPatient();
	}

	@Override
	public void setPatient(boolean value) {
		getEntity().setPatient(value);
	}

	@Override
	public boolean isLaboratory() {
		return getEntity().isLaboratory();
	}

	@Override
	public void setLaboratory(boolean value) {
		getEntity().setLaboratory(value);
	}

	@Override
	public boolean isOrganization() {
		return getEntity().isOrganisation();
	}

	@Override
	public void setOrganization(boolean value) {
		getEntity().setOrganisation(value);
	}

	@Override
	public String getDescription1() {
		return getEntity().getDescription1();
	}

	@Override
	public void setDescription1(String value) {
		getEntity().setDescription1(value);
	}

	@Override
	public String getDescription2() {
		return getEntity().getDescription2();
	}

	@Override
	public void setDescription2(String value) {
		getEntity().setDescription2(value);
	}

	@Override
	public String getDescription3() {
		return getEntity().getDescription3();
	}

	@Override
	public void setDescription3(String value) {
		getEntity().setDescription3(value);
	}

	@Override
	public String getCode() {
		return getEntity().getCode();
	}

	@Override
	public void setCode(String value) {
		getEntity().setCode(value);
	}

	@Override
	public Country getCountry() {
		return getEntity().getCountry();
	}

	@Override
	public void setCountry(Country value) {
		getEntity().setCountry(value);
	}

	@Override
	public String getZip() {
		return getEntity().getZip();
	}

	@Override
	public void setZip(String value) {
		getEntity().setZip(value);
	}

	@Override
	public String getCity() {
		return getEntity().getCity();
	}

	@Override
	public void setCity(String value) {
		getEntity().setCity(value);
	}

	@Override
	public String getStreet() {
		return getEntity().getStreet();
	}

	@Override
	public void setStreet(String value) {
		getEntity().setStreet(value);
	}

	@Override
	public String getPhone1() {
		return getEntity().getPhone1();
	}

	@Override
	public void setPhone1(String value) {
		getEntity().setPhone1(value);
	}

	@Override
	public String getPhone2() {
		return getEntity().getPhone2();
	}

	@Override
	public void setPhone2(String value) {
		getEntity().setPhone2(value);
	}

	@Override
	public String getFax() {
		return getEntity().getFax();
	}

	@Override
	public void setFax(String value) {
		getEntity().setFax(value);
	}

	@Override
	public String getEmail() {
		return getEntity().getEmail();
	}

	@Override
	public void setEmail(String value) {
		getEntity().setEmail(value);
	}

	@Override
	public String getWebsite() {
		return getEntity().getWebsite();
	}

	@Override
	public void setWebsite(String value) {
		getEntity().setWebsite(value);
	}

	@Override
	public String getMobile() {
		return getEntity().getMobile();
	}

	@Override
	public void setMobile(String value) {
		getEntity().setMobile(value);
	}

	@Override
	public String getComment() {
		return getEntity().getComment();
	}

	@Override
	public void setComment(String value) {
		getEntity().setComment(value);
	}

	@Override
	public String getGroup() {
		return getEntity().getGruppe();
	}

	@Override
	public void setGroup(String value) {
		getEntity().setGruppe(value);
	}

	@Override
	public String getLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDescription1()).append(" ").append(StringUtils.defaultString(getDescription2()));
		if (!StringUtils.isBlank(getDescription3())) {
			sb.append("(").append(getDescription3()).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append(", ").append(StringUtils.defaultString(getStreet())).append(", ") //$NON-NLS-1$ //$NON-NLS-2$
				.append(StringUtils.defaultString(getZip())).append(" ").append(StringUtils.defaultString(getCity()));
		return sb.toString();
	}

	@Override
	public Object getExtInfo(Object key) {
		return extInfoHandler.getExtInfo(key);
	}

	@Override
	public void setExtInfo(Object key, Object value) {
		extInfoHandler.setExtInfo(key, value);
	}

	@Override
	public List<IAddress> getAddress() {
		CoreModelServiceHolder.get().refresh(this);
		ArrayList<ZusatzAdresse> addresses = new ArrayList<>(getEntity().getAddresses().values());
		return addresses.parallelStream().filter(f -> !f.isDeleted())
				.map(f -> ModelUtil.getAdapter(f, IAddress.class, true)).collect(Collectors.toList());
	}

	@Override
	public IAddress addAddress(IAddress address) {
		address.setContact(this);
		@SuppressWarnings("unchecked")
		ZusatzAdresse addresse = ((AbstractIdModelAdapter<ZusatzAdresse>) address).getEntity();
		getEntity().getAddresses().put(address.getId(), addresse);
		return address;
	}

	@Override
	public String getPostalAddress() {
		// TODO Auto-generated method stub
		return getEntity().getAnschrift();
	}

	@Override
	public void setPostalAddress(String value) {
		// TODO Auto-generated method stub
	}

	@Override
	public IImage getImage() {
		return CoreModelServiceHolder.get().load(getId(), IImage.class).orElse(null);
	}

	@Override
	public void setImage(IImage value) {
		IImage image = getImage();
		if (value == null) {
			if (image != null) {
				CoreModelServiceHolder.get().remove(image);
			}
			return;
		}
		if (image == null) {
			image = CoreModelServiceHolder.get().create(IImage.class);
			image.setId(getId());
			image.setTitle("ContactImage");
		}
		image.setDate(value.getDate());
		image.setPrefix("ch.elexis.data.Kontakt");
		image.setImage(value.getImage());
		image.setMimeType(value.getMimeType());
		CoreModelServiceHolder.get().save(image);
	}

	@Override
	public List<IRelatedContact> getRelatedContacts() {
		CoreModelServiceHolder.get().refresh(this);
		ArrayList<KontaktAdressJoint> relatedContacts = new ArrayList<>(getEntity().getRelatedContacts().values());
		return relatedContacts.parallelStream().filter(f -> !f.isDeleted())
				.map(f -> ModelUtil.getAdapter(f, IRelatedContact.class, true)).collect(Collectors.toList());
	}

	@Override
	public IRelatedContact addRelatedContact(IRelatedContact relatedContact) {
		relatedContact.setMyContact(this);
		@SuppressWarnings("unchecked")
		KontaktAdressJoint kaj = ((AbstractIdModelAdapter<KontaktAdressJoint>) relatedContact).getEntity();
		getEntity().getRelatedContacts().put(kaj.getId(), kaj);
		return relatedContact;
	}
}

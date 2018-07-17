/**
 * Copyright (c) 2013 MEDEVIT <office@medevit.at>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 */
package ch.elexis.core.types.impl;

import ch.elexis.core.model.LabOrderState;
import ch.elexis.core.model.XidQuality;
import java.time.LocalDateTime;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import ch.elexis.core.types.AddressType;
import ch.elexis.core.types.ContactGender;
import ch.elexis.core.types.Country;
import ch.elexis.core.types.DocumentStatus;
import ch.elexis.core.types.Gender;
import ch.elexis.core.types.LabItemTyp;
import ch.elexis.core.types.PathologicDescription;
import ch.elexis.core.types.RelationshipType;
import ch.elexis.core.types.TypesFactory;
import ch.elexis.core.types.TypesPackage;
import ch.rgw.tools.Money;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TypesFactoryImpl extends EFactoryImpl implements TypesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TypesFactory init() {
		try {
			TypesFactory theTypesFactory = (TypesFactory)EPackage.Registry.INSTANCE.getEFactory(TypesPackage.eNS_URI);
			if (theTypesFactory != null) {
				return theTypesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TypesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case TypesPackage.CONTACT_GENDER:
				return createContactGenderFromString(eDataType, initialValue);
			case TypesPackage.RELATIONSHIP_TYPE:
				return createRelationshipTypeFromString(eDataType, initialValue);
			case TypesPackage.ADDRESS_TYPE:
				return createAddressTypeFromString(eDataType, initialValue);
			case TypesPackage.DOCUMENT_STATUS:
				return createDocumentStatusFromString(eDataType, initialValue);
			case TypesPackage.MONEY:
				return createMoneyFromString(eDataType, initialValue);
			case TypesPackage.GENDER:
				return createGenderFromString(eDataType, initialValue);
			case TypesPackage.LAB_ITEM_TYP:
				return createLabItemTypFromString(eDataType, initialValue);
			case TypesPackage.COUNTRY:
				return createCountryFromString(eDataType, initialValue);
			case TypesPackage.PATHOLOGIC_DESCRIPTION:
				return createPathologicDescriptionFromString(eDataType, initialValue);
			case TypesPackage.LOCAL_DATE_TIME:
				return createLocalDateTimeFromString(eDataType, initialValue);
			case TypesPackage.INPUT_STREAM:
				return createInputStreamFromString(eDataType, initialValue);
			case TypesPackage.OUTPUT_STREAM:
				return createOutputStreamFromString(eDataType, initialValue);
			case TypesPackage.LOCAL_DATE:
				return createLocalDateFromString(eDataType, initialValue);
			case TypesPackage.XID_QUALITY:
				return createXidQualityFromString(eDataType, initialValue);
			case TypesPackage.LAB_ORDER_STATE:
				return createLabOrderStateFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case TypesPackage.CONTACT_GENDER:
				return convertContactGenderToString(eDataType, instanceValue);
			case TypesPackage.RELATIONSHIP_TYPE:
				return convertRelationshipTypeToString(eDataType, instanceValue);
			case TypesPackage.ADDRESS_TYPE:
				return convertAddressTypeToString(eDataType, instanceValue);
			case TypesPackage.DOCUMENT_STATUS:
				return convertDocumentStatusToString(eDataType, instanceValue);
			case TypesPackage.MONEY:
				return convertMoneyToString(eDataType, instanceValue);
			case TypesPackage.GENDER:
				return convertGenderToString(eDataType, instanceValue);
			case TypesPackage.LAB_ITEM_TYP:
				return convertLabItemTypToString(eDataType, instanceValue);
			case TypesPackage.COUNTRY:
				return convertCountryToString(eDataType, instanceValue);
			case TypesPackage.PATHOLOGIC_DESCRIPTION:
				return convertPathologicDescriptionToString(eDataType, instanceValue);
			case TypesPackage.LOCAL_DATE_TIME:
				return convertLocalDateTimeToString(eDataType, instanceValue);
			case TypesPackage.INPUT_STREAM:
				return convertInputStreamToString(eDataType, instanceValue);
			case TypesPackage.OUTPUT_STREAM:
				return convertOutputStreamToString(eDataType, instanceValue);
			case TypesPackage.LOCAL_DATE:
				return convertLocalDateToString(eDataType, instanceValue);
			case TypesPackage.XID_QUALITY:
				return convertXidQualityToString(eDataType, instanceValue);
			case TypesPackage.LAB_ORDER_STATE:
				return convertLabOrderStateToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContactGender createContactGenderFromString(EDataType eDataType, String initialValue) {
		ContactGender result = ContactGender.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertContactGenderToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationshipType createRelationshipTypeFromString(EDataType eDataType, String initialValue) {
		RelationshipType result = RelationshipType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRelationshipTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddressType createAddressTypeFromString(EDataType eDataType, String initialValue) {
		AddressType result = AddressType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAddressTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentStatus createDocumentStatusFromString(EDataType eDataType, String initialValue) {
		DocumentStatus result = DocumentStatus.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDocumentStatusToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Money createMoneyFromString(EDataType eDataType, String initialValue) {
		return (Money)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMoneyToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Gender createGenderFromString(EDataType eDataType, String initialValue) {
		return (Gender)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertGenderToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LabItemTyp createLabItemTypFromString(EDataType eDataType, String initialValue) {
		return (LabItemTyp)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLabItemTypToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Country createCountryFromString(EDataType eDataType, String initialValue) {
		return (Country)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCountryToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathologicDescription createPathologicDescriptionFromString(EDataType eDataType, String initialValue) {
		return (PathologicDescription)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPathologicDescriptionToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDateTime createLocalDateTimeFromString(EDataType eDataType, String initialValue) {
		return (LocalDateTime)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLocalDateTimeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputStream createInputStreamFromString(EDataType eDataType, String initialValue) {
		return (InputStream)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInputStreamToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputStream createOutputStreamFromString(EDataType eDataType, String initialValue) {
		return (OutputStream)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOutputStreamToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate createLocalDateFromString(EDataType eDataType, String initialValue) {
		return (LocalDate)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLocalDateToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XidQuality createXidQualityFromString(EDataType eDataType, String initialValue) {
		return (XidQuality)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertXidQualityToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LabOrderState createLabOrderStateFromString(EDataType eDataType, String initialValue) {
		return (LabOrderState)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLabOrderStateToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypesPackage getTypesPackage() {
		return (TypesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TypesPackage getPackage() {
		return TypesPackage.eINSTANCE;
	}

} //TypesFactoryImpl

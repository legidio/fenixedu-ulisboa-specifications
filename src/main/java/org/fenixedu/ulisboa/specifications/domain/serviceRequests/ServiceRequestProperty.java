package org.fenixedu.ulisboa.specifications.domain.serviceRequests;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.fenixedu.ulisboa.specifications.dto.ServiceRequestPropertyBean;
import org.fenixedu.ulisboa.specifications.util.ULisboaConstants;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

public class ServiceRequestProperty extends ServiceRequestProperty_Base {

    public static final Map<String, Object> propertyNames = new HashMap<>();

    static {
        initPropertyNames();
    }

    public static final Comparator<ServiceRequestProperty> COMPARATE_BY_ENTRY_NUMBER = new Comparator<ServiceRequestProperty>() {

        @Override
        public int compare(ServiceRequestProperty o1, ServiceRequestProperty o2) {
            ServiceRequestSlotEntry entry1 = ServiceRequestSlotEntry.findByServiceRequestProperty(o1);
            ServiceRequestSlotEntry entry2 = ServiceRequestSlotEntry.findByServiceRequestProperty(o2);
            if (entry1 == null) {
                return 1;
            }
            if (entry2 == null) {
                return -1;
            }
            return Integer.compare(entry1.getOrderNumber(), entry2.getOrderNumber());
        }
    };

    protected ServiceRequestProperty() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected ServiceRequestProperty(ServiceRequestSlot serviceRequestSlot) {
        this();
        setServiceRequestSlot(serviceRequestSlot);
        checkRules();
    }

    private void checkRules() {

        if (getServiceRequestSlot() == null) {
            throw new ULisboaSpecificationsDomainException("error.ServiceRequestProperty.serviceRequestSlot.required");
        }
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
    }

    @Atomic
    public void delete() {
        ULisboaSpecificationsDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        setULisboaServiceRequest(null);
        setDocumentPurposeTypeInstance(null);
        setExecutionYear(null);
        setServiceRequestSlot(null);
        super.getCurriculumLinesSet().clear();
        super.getExternalEnrolmentsSet().clear();

        setBennu(null);
        deleteDomainObject();
    }

    public List<ICurriculumEntry> getICurriculumEntriesSet() {
        Stream<ICurriculumEntry> curriculumEntries = super.getCurriculumLinesSet().stream().map(ICurriculumEntry.class::cast);
        Stream<ICurriculumEntry> externalEntries = super.getExternalEnrolmentsSet().stream().map(ICurriculumEntry.class::cast);
        return Stream.concat(curriculumEntries, externalEntries).collect(Collectors.toList());
    }

    public void setICurriculumEntriesSet(List<ICurriculumEntry> entries) {
        getCurriculumLinesSet().clear();
        getExternalEnrolmentsSet().clear();
        for (ICurriculumEntry entry : entries) {
            if (entry instanceof CurriculumLine) {
                addCurriculumLines((CurriculumLine) entry);
            } else if (entry instanceof ExternalEnrolment) {
                addExternalEnrolments((ExternalEnrolment) entry);
            } else {
                throw new ULisboaSpecificationsDomainException("error.ServiceRequestProperty.curriculumEntry.not.supported");
            }
        }
    }

    public void setValue(Object value) {
        String propertyName = getPropertyName(getServiceRequestSlot());
        try {
            BeanUtils.setProperty(this, propertyName, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ULisboaSpecificationsDomainException(e, "error.serviceRequests.ServiceRequestProperty.write.slot",
                    getServiceRequestSlot().getCode(), getServiceRequestSlot().getUiComponentType().toString(), propertyName);
        }
    }

    public <T> T getValue() {
        ServiceRequestSlot slot = getServiceRequestSlot();
        if (slot.getUiComponentType() == null || slot.getCode() == null) {
            throw new ULisboaSpecificationsDomainException(
                    "error.serviceRequests.ServiceRequestPropertyBean.uicomponent.code.not.defined");
        }
        String pName = getPropertyName(slot);
        try {
            return (T) PropertyUtils.getProperty(this, pName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ULisboaSpecificationsDomainException(e, "error.serviceRequests.ServiceRequestProperty.read.slot",
                    slot.getCode(), slot.getUiComponentType().toString(), pName);
        }
    }

    public boolean isNullOrEmpty() {
        if (getValue() == null) {
            return true;
        }
        Object value = getValue();
        if (getServiceRequestSlot().getUiComponentType() == UIComponentType.TEXT) {
            return Strings.isNullOrEmpty((String) value);
        }
        if (getServiceRequestSlot().getUiComponentType() == UIComponentType.TEXT_LOCALIZED_STRING) {
            return ((LocalizedString) value).isEmpty();
        }
        if (getServiceRequestSlot().getUiComponentType() == UIComponentType.DROP_DOWN_MULTIPLE) {
            return ((Collection) value).isEmpty();
        }
        return false;
    }

    public static Stream<ServiceRequestProperty> findAll() {
        return Bennu.getInstance().getServiceRequestPropertiesSet().stream();
    }

    public static Stream<ServiceRequestProperty> findByCode(String code) {
        return ServiceRequestProperty.findAll().filter(prop -> prop.getServiceRequestSlot().getCode().equals(code));
    }

    @Atomic
    public static ServiceRequestProperty create(ServiceRequestSlot slot) {
        return new ServiceRequestProperty(slot);
    }

    @Atomic
    public static ServiceRequestProperty create(ServiceRequestSlot slot, Object value) {
        if (value == null) {
            return create(slot);
        }
        ServiceRequestProperty property = new ServiceRequestProperty(slot);
        property.setValue(value);
        return property;
    }

    @Atomic
    public static ServiceRequestProperty create(ServiceRequestPropertyBean bean) {
        ServiceRequestSlot slot = ServiceRequestSlot.getByCode(bean.getCode());
        Object value = bean.getValue();
        return create(slot, value);
    }

    private static String getPropertyName(ServiceRequestSlot slot) {
        Object propertyName = propertyNames.get(slot.getUiComponentType().toString());
        if (propertyName instanceof Map) {
            return ((Map<String, String>) propertyName).get(slot.getCode());
        } else {
            return (String) propertyName;
        }
    }

    private static void initPropertyNames() {
        propertyNames.put(UIComponentType.DROP_DOWN_BOOLEAN.toString(), "booleanValue");
        propertyNames.put(UIComponentType.NUMBER.toString(), "integer");
        propertyNames.put(UIComponentType.TEXT.toString(), "string");
        propertyNames.put(UIComponentType.TEXT_LOCALIZED_STRING.toString(), "localizedString");
        propertyNames.put(UIComponentType.DATE.toString(), "dateTime");
        propertyNames.put(UIComponentType.DROP_DOWN_MULTIPLE.toString(), "ICurriculumEntriesSet");
        Map<String, String> dropDownPropertyNames = new HashMap<>();
        dropDownPropertyNames.put(ULisboaConstants.LANGUAGE, "locale");
        dropDownPropertyNames.put(ULisboaConstants.DOCUMENT_PURPOSE_TYPE, "documentPurposeTypeInstance");
        dropDownPropertyNames.put(ULisboaConstants.CYCLE_TYPE, "cycleType");
        dropDownPropertyNames.put(ULisboaConstants.PROGRAM_CONCLUSION, "programConclusion");
        dropDownPropertyNames.put(ULisboaConstants.EXECUTION_YEAR, "executionYear");
        dropDownPropertyNames.put(ULisboaConstants.CURRICULAR_PLAN, "studentCurricularPlan");
        propertyNames.put(UIComponentType.DROP_DOWN_ONE_VALUE.toString(), dropDownPropertyNames);
    }

}

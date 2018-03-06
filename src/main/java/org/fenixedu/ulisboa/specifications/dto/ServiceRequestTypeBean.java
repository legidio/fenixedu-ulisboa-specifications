package org.fenixedu.ulisboa.specifications.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.serviceRequests.ServiceRequestCategory;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestOutputType;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestSlot;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.processors.ULisboaServiceRequestProcessor;

public class ServiceRequestTypeBean implements IBean {

    private String code;
    private LocalizedString name;
    private boolean active;
    private boolean payable;
    private boolean notifyUponConclusion;
    private boolean printable;
    private boolean requestedOnline;
    private boolean printableOnline;
    private ServiceRequestCategory serviceRequestCategory;
    private List<TupleDataSourceBean> serviceRequestCategoryDataSource;
    private List<TupleDataSourceBean> serviceRequestSlotsDataSource;
    private List<ULisboaServiceRequestProcessor> processors;
    private List<TupleDataSourceBean> processorsDataSource;
    private LocalizedString numberOfUnitsLabel;
    private ServiceRequestOutputType documentGeneratedOutputType;
    private List<TupleDataSourceBean> documentGeneratedOutputTypeDataSource;

    // The following class is used for importation

    public static class SlotRequestBean {
        private ServiceRequestSlot slot;
        private boolean required;
        private boolean printConfiguration;
        private Object slotDefaultValue;

        public ServiceRequestSlot getSlot() {
            return slot;
        }

        public void setSlot(final ServiceRequestSlot slot) {
            this.slot = slot;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(final boolean required) {
            this.required = required;
        }

        public boolean isPrintConfiguration() {
            return printConfiguration;
        }

        public void setPrintConfiguration(final boolean printConfiguration) {
            this.printConfiguration = printConfiguration;
        }

        public Object getSlotDefaultValue() {
            return slotDefaultValue;
        }

        public void setSlotDefaultValue(final Object slotDefaultValue) {
            this.slotDefaultValue = slotDefaultValue;
        }

    }

    private List<SlotRequestBean> requestSlotsBeans;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public LocalizedString getName() {
        return name;
    }

    public void setName(final LocalizedString name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(final boolean payable) {
        this.payable = payable;
    }

    public boolean isNotifyUponConclusion() {
        return notifyUponConclusion;
    }

    public void setNotifyUponConclusion(final boolean notifyUponConclusion) {
        this.notifyUponConclusion = notifyUponConclusion;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(final boolean printable) {
        this.printable = printable;
    }

    public boolean isRequestedOnline() {
        return requestedOnline;
    }

    public void setRequestedOnline(final boolean requestedOnline) {
        this.requestedOnline = requestedOnline;
    }

    public boolean isPrintableOnline() {
        return printableOnline;
    }

    public void setPrintableOnline(final boolean printableOnline) {
        this.printableOnline = printableOnline;
    }

    public ServiceRequestCategory getServiceRequestCategory() {
        return serviceRequestCategory;
    }

    public void setServiceRequestCategory(final ServiceRequestCategory serviceRequestCategory) {
        this.serviceRequestCategory = serviceRequestCategory;
    }

    public List<TupleDataSourceBean> getServiceRequestCategoryDataSource() {
        return serviceRequestCategoryDataSource;
    }

    public void setServiceRequestCategoryDataSource(final List<ServiceRequestCategory> serviceRequestCategorySet) {
        this.serviceRequestCategoryDataSource = serviceRequestCategorySet.stream().map(src -> {
            TupleDataSourceBean tupleDataSourceBean = new TupleDataSourceBean();
            tupleDataSourceBean.setId(src.toString());
            tupleDataSourceBean.setText(src.toString());
            return tupleDataSourceBean;
        }).collect(Collectors.toList());
    }

    public List<TupleDataSourceBean> getServiceRequestSlotsDataSource() {
        return serviceRequestSlotsDataSource;
    }

    public void setServiceRequestSlotsDataSource(final List<ServiceRequestSlot> serviceRequestSlotsSet) {
        this.serviceRequestSlotsDataSource = serviceRequestSlotsSet.stream().map(srs -> {
            TupleDataSourceBean tupleDataSourceBean = new TupleDataSourceBean();
            tupleDataSourceBean.setId(srs.getExternalId());
            tupleDataSourceBean.setText(srs.getCode());
            return tupleDataSourceBean;
        }).collect(Collectors.toList());
    }

    public List<ULisboaServiceRequestProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(final List<ULisboaServiceRequestProcessor> validators) {
        this.processors = validators;
    }

    public List<TupleDataSourceBean> getProcessorsDataSource() {
        return processorsDataSource;
    }

    public void setProcessorsDataSource(final List<ULisboaServiceRequestProcessor> processorsSet) {
        this.processorsDataSource = processorsSet.stream().map(srs -> {
            TupleDataSourceBean tupleDataSourceBean = new TupleDataSourceBean();
            tupleDataSourceBean.setId(srs.getExternalId());
            tupleDataSourceBean.setText(srs.getName().getContent());
            return tupleDataSourceBean;
        }).collect(Collectors.toList());
    }

    public LocalizedString getNumberOfUnitsLabel() {
        return numberOfUnitsLabel;
    }

    public void setNumberOfUnitsLabel(final LocalizedString numberOfUnitsLabel) {
        this.numberOfUnitsLabel = numberOfUnitsLabel;
    }

    public ServiceRequestOutputType getDocumentGeneratedOutputType() {
        return documentGeneratedOutputType;
    }

    public void setDocumentGeneratedOutputType(final ServiceRequestOutputType documentGeneratedOutputType) {
        this.documentGeneratedOutputType = documentGeneratedOutputType;
    }

    public List<TupleDataSourceBean> getDocumentGeneratedOutputTypeDataSource() {
        return documentGeneratedOutputTypeDataSource;
    }

    public void setDocumentGeneratedOutputTypeDataSource(final List<ServiceRequestOutputType> documentGeneratedOutputTypes) {
        this.documentGeneratedOutputTypeDataSource = documentGeneratedOutputTypes.stream().map(mimeT -> {
            TupleDataSourceBean tupleDataSourceBean = new TupleDataSourceBean();
            tupleDataSourceBean.setId(mimeT.getExternalId());
            tupleDataSourceBean.setText(mimeT.getLabel().getContent());
            return tupleDataSourceBean;
        }).collect(Collectors.toList());
    }

    public List<SlotRequestBean> getRequestSlotsBeans() {
        return requestSlotsBeans;
    }

    public void setRequestSlotsBeans(final List<SlotRequestBean> requestSlotsBeans) {
        this.requestSlotsBeans = requestSlotsBeans;
    }

    public ServiceRequestTypeBean() {
        setServiceRequestCategoryDataSource(Arrays.asList(ServiceRequestCategory.values()));
        setServiceRequestSlotsDataSource(ServiceRequestSlot.findAll().collect(Collectors.toList()));
        setProcessorsDataSource(ULisboaServiceRequestProcessor.findAll().collect(Collectors.toList()));
        setDocumentGeneratedOutputTypeDataSource(
                Bennu.getInstance().getServiceRequestOutputTypesSet().stream().collect(Collectors.toList()));
        processors = new ArrayList<>();
    }

    public ServiceRequestTypeBean(final ServiceRequestType serviceRequestType) {
        this();
        setCode(serviceRequestType.getCode());
        setName(serviceRequestType.getName());
        setActive(serviceRequestType.getActive());
        setPayable(serviceRequestType.isPayable());
        setNotifyUponConclusion(serviceRequestType.isToNotifyUponConclusion());
        setPrintable(serviceRequestType.isPrintable());
        setRequestedOnline(serviceRequestType.isRequestedOnline());
        setServiceRequestCategory(serviceRequestType.getServiceRequestCategory());
        setProcessors(serviceRequestType.getULisboaServiceRequestProcessorsSet().stream().collect(Collectors.toList()));
        setNumberOfUnitsLabel(serviceRequestType.getNumberOfUnitsLabel());
        setDocumentGeneratedOutputType(serviceRequestType.getServiceRequestOutputType());
    }
}

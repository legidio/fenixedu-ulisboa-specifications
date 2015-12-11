/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: diogo.simoes@qub-it.com
 *               jnpa@reitoria.ulisboa.pt
 *
 * 
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.ulisboa.specifications.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestRestriction;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestSlot;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestSlotEntry;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.UIComponentType;
import org.fenixedu.ulisboa.specifications.util.ULisboaConstants;
import org.joda.time.DateTime;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ULisboaServiceRequestBean implements IBean {

    public static final Map<String, DataSourceProvider> DATA_SOURCE_PROVIDERS = new HashMap<String, DataSourceProvider>();

    static {
        initProviderMap();
    }

    private Registration registration;
    private ServiceRequestType serviceRequestType;
    private List<TupleDataSourceBean> serviceRequestTypesDataSource;
    private List<ServiceRequestPropertyBean> serviceRequestPropertyBeans;
    private boolean requestedOnline;

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public ServiceRequestType getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(ServiceRequestType serviceRequestType) {
        this.serviceRequestType = serviceRequestType;
    }

    public List<TupleDataSourceBean> getServiceRequestTypesDataSource() {
        return serviceRequestTypesDataSource;
    }

    public void setServiceRequestTypesDataSource(List<ServiceRequestType> documentTypesValues) {
        this.serviceRequestTypesDataSource = documentTypesValues.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            if (x.getRequestedOnline() != null && x.getRequestedOnline() && this.isRequestedOnline()) {
                tuple.setText(x.getRichName());
            } else {
                tuple.setText(x.getName().getContent());
            }
            return tuple;
        }).collect(Collectors.toList());
    }

    public List<ServiceRequestPropertyBean> getServiceRequestPropertyBeans() {
        return serviceRequestPropertyBeans;
    }

    public void setServiceRequestPropertyBeans(List<ServiceRequestPropertyBean> serviceRequestPropertyBeans) {
        this.serviceRequestPropertyBeans = serviceRequestPropertyBeans;
    }

    public <T> T getServiceRequestPropertyValue(String code) {
        ServiceRequestPropertyBean serviceRequestPropertyBean =
                getServiceRequestPropertyBeans().stream().filter(s -> s.getCode().equals(code)).findFirst().get();

        return (T) ServiceRequestSlot.convertValue(code, serviceRequestPropertyBean.getValue());
    }

    public boolean isRequestedOnline() {
        return requestedOnline;
    }

    public void setRequestedOnline(boolean requestedOnline) {
        this.requestedOnline = requestedOnline;
    }

    public ULisboaServiceRequestBean() {
        setServiceRequestPropertyBeans(new ArrayList<ServiceRequestPropertyBean>());
    }

    public ULisboaServiceRequestBean(Registration registration, boolean requestedOnline) {
        this();
        setRegistration(registration);
        setRequestedOnline(requestedOnline);
        setServiceRequestTypesDataSource(ServiceRequestType.findActive()
                .filter(ServiceRequestRestriction.restrictionFilter(registration))
                .filter(srt -> !requestedOnline || srt.isRequestedOnline())
                .sorted(ServiceRequestType.COMPARE_BY_CATEGORY_THEN_BY_NAME).collect(Collectors.toList()));
    }

    private boolean isSameServiceRequestType() {
        Set<String> oldSlotNames =
                serviceRequestPropertyBeans.stream().map(ServiceRequestPropertyBean::getCode).collect(Collectors.toSet());
        Set<String> newSlotNames =
                serviceRequestType.getServiceRequestSlotEntriesSet().stream()
                        .map(entry -> entry.getServiceRequestSlot().getCode()).collect(Collectors.toSet());
        return oldSlotNames.size() == newSlotNames.size() && Sets.difference(oldSlotNames, newSlotNames).isEmpty();
    }

    public void updateModelLists() {
        //update service request type
        if (serviceRequestType == null) {
            serviceRequestPropertyBeans = new ArrayList<ServiceRequestPropertyBean>();
        } else if (!isSameServiceRequestType()) {
            serviceRequestPropertyBeans = new ArrayList<ServiceRequestPropertyBean>();
            serviceRequestType.getServiceRequestSlotEntriesSet().stream().sorted(ServiceRequestSlotEntry.COMPARE_BY_ORDER_NUMBER)
                    .forEachOrdered(entry -> serviceRequestPropertyBeans.add(new ServiceRequestPropertyBean(entry)));
        }
        //update properties
        for (ServiceRequestPropertyBean serviceRequestPropertyBean : serviceRequestPropertyBeans) {
            if (serviceRequestPropertyBean.getUiComponent().needDataSource()) {
                DataSourceProvider dataSourceProvider =
                        ULisboaServiceRequestBean.DATA_SOURCE_PROVIDERS.get(serviceRequestPropertyBean.getCode());
                if (dataSourceProvider == null) {
                    throw new RuntimeException("error.provider.not.defined");
                }
                serviceRequestPropertyBean.setDataSource(dataSourceProvider.provideDataSourceList(this));
                if (serviceRequestPropertyBean.getDataSource().size() == 1 && serviceRequestPropertyBean.isRequired()) {
                    //Only one option, populate the component
                    //DropDown One Value
                    if (serviceRequestPropertyBean.getUiComponent() == UIComponentType.DROP_DOWN_ONE_VALUE
                            || serviceRequestPropertyBean.getUiComponent() == UIComponentType.DROP_DOWN_BOOLEAN) {
                        serviceRequestPropertyBean.setValue(serviceRequestPropertyBean.getDataSource().get(0).getId());
                    }
                    //DropDown Multiple Values
                    if (serviceRequestPropertyBean.getUiComponent() == UIComponentType.DROP_DOWN_MULTIPLE) {
                        JsonObject object = new JsonObject();
                        object.addProperty("id", serviceRequestPropertyBean.getDataSource().get(0).getId());
                        JsonArray array = new JsonArray();
                        array.add(object);
                        serviceRequestPropertyBean.setValue(array.toString());
                    }
                }
            }
        }
    }

    private static List<TupleDataSourceBean> provideForCurriculumEntry(Stream<ICurriculumEntry> collection) {
        return collection.sorted((x, y) -> x.getName().compareTo(y.getName())).map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getCode() + " - " + x.getName().getContent() + " - " + x.getExecutionYear().getQualifiedName());
            return tuple;
        }).collect(Collectors.toList());
    }

    public static void initProviderMap() {
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.LANGUAGE, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                return CoreConfiguration.supportedLocales().stream().map(x -> {
                    TupleDataSourceBean tuple = new TupleDataSourceBean();
                    tuple.setId(x.toString().replace("_", "-"));
                    tuple.setText(x.getDisplayLanguage());
                    return tuple;
                }).collect(Collectors.toList());
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.DOCUMENT_PURPOSE_TYPE, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                return DocumentPurposeTypeInstance.findActivesFor(bean.getServiceRequestType())
                        .sorted(DocumentPurposeTypeInstance.COMPARE_BY_LEGACY).map(x -> {
                            TupleDataSourceBean tuple = new TupleDataSourceBean();
                            tuple.setId(x.getExternalId());
                            tuple.setText(x.getName().getContent());
                            return tuple;
                        }).collect(Collectors.toList());
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.CYCLE_TYPE, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                if (bean.getRegistration().getDegreeType() == null) {
                    return Collections.emptyList();
                }
                return bean.getRegistration().getDegreeType().getCycleTypes().stream()
                        .sorted(CycleType.COMPARATOR_BY_LESS_WEIGHT).map(x -> {
                            TupleDataSourceBean tuple = new TupleDataSourceBean();
                            tuple.setId(x.toString());
                            tuple.setText(x.getDescription());
                            return tuple;
                        }).collect(Collectors.toList());
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.PROGRAM_CONCLUSION, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                if (ProgramConclusion.conclusionsFor(bean.getRegistration()).count() == 0) {
                    return Collections.emptyList();
                }
                return ProgramConclusion.conclusionsFor(bean.getRegistration()).map(x -> {
                    TupleDataSourceBean tuple = new TupleDataSourceBean();
                    tuple.setId(x.getExternalId());
                    tuple.setText(x.getName().getContent());
                    return tuple;
                }).collect(Collectors.toList());
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.CURRICULAR_PLAN, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {

                final ExecutionYear executionYear = bean.getServiceRequestPropertyValue(ULisboaConstants.EXECUTION_YEAR);
                if (executionYear == null) {
                    return bean.getRegistration().getStudentCurricularPlansSet().stream()
                            .sorted((x, y) -> x.getName().compareTo(y.getName())).map(x -> {
                                TupleDataSourceBean tuple = new TupleDataSourceBean();
                                tuple.setId(x.getExternalId());
                                tuple.setText(x.getName());
                                return tuple;
                            }).collect(Collectors.toList());
                }

                final StudentCurricularPlan studentCurricularPlan =
                        bean.getRegistration().getStudentCurricularPlan(executionYear);
                final TupleDataSourceBean result = new TupleDataSourceBean();
                if (studentCurricularPlan != null) {
                    result.setId(studentCurricularPlan.getExternalId());
                    result.setText(studentCurricularPlan.getName());
                }

                return Collections.singletonList(result);
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.APPROVED_EXTRA_CURRICULUM, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                if (bean.getRegistration().getLastStudentCurricularPlan() == null
                        || bean.getRegistration().getLastStudentCurricularPlan().getExtraCurriculumGroup() == null) {
                    return Collections.emptyList();
                }
                Stream<ICurriculumEntry> collection =
                        bean.getRegistration().getLastStudentCurricularPlan().getExtraCurriculumGroup().getEnrolmentsSet()
                                .stream().map(ICurriculumEntry.class::cast);
                return provideForCurriculumEntry(collection);
            }

        });

        //TODOJN: adicionar todos os enrolments de todos os Standalones do aluno?
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.APPROVED_STANDALONE_CURRICULUM, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                if (bean.getRegistration().getLastStudentCurricularPlan() == null
                        || bean.getRegistration().getLastStudentCurricularPlan().getStandaloneCurriculumGroup() == null) {
                    return Collections.emptyList();
                }
                Stream<ICurriculumEntry> collection =
                        bean.getRegistration().getLastStudentCurricularPlan().getStandaloneCurriculumGroup().getEnrolmentsSet()
                                .stream().map(ICurriculumEntry.class::cast);
                return provideForCurriculumEntry(collection);
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.APPROVED_ENROLMENTS, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                if (bean.getRegistration().getLastStudentCurricularPlan() == null) {
                    return Collections.emptyList();
                }
                final Curriculum curriculum =
                        bean.getRegistration().getLastStudentCurricularPlan().getCurriculum(new DateTime(), null);
                Stream<ICurriculumEntry> collection =
                        curriculum.getCurriculumEntries().stream().filter(e -> e instanceof Enrolment);
                return provideForCurriculumEntry(collection);
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.CURRICULUM, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                if (bean.getRegistration().getLastStudentCurricularPlan() == null) {
                    return Collections.emptyList();
                }
                final Curriculum curriculum =
                        bean.getRegistration().getLastStudentCurricularPlan().getCurriculum(new DateTime(), null);
                Stream<ICurriculumEntry> collection = curriculum.getCurriculumEntries().stream();
                return provideForCurriculumEntry(collection);
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.ENROLMENTS_BY_YEAR, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                final ExecutionYear executionYear = bean.getServiceRequestPropertyValue(ULisboaConstants.EXECUTION_YEAR);
                if (executionYear == null || bean.getRegistration().getStudentCurricularPlan(executionYear) == null) {
                    return Collections.emptyList();
                }
                Stream<ICurriculumEntry> collection =
                        bean.getRegistration().getStudentCurricularPlan(executionYear)
                                .getEnrolmentsByExecutionYear(executionYear).stream()
                                .filter(e -> !e.getCurriculumGroup().isNoCourseGroupCurriculumGroup() && !e.isAnnulled())
                                .sorted(Enrolment.COMPARATOR_BY_NAME_AND_ID).map(ICurriculumEntry.class::cast);
                return provideForCurriculumEntry(collection);
            }
        });
        DATA_SOURCE_PROVIDERS.put(ULisboaConstants.EXECUTION_YEAR, new DataSourceProvider() {

            @Override
            public List<TupleDataSourceBean> provideDataSourceList(ULisboaServiceRequestBean bean) {
                return ExecutionYear.readNotClosedExecutionYears().stream()
                        .filter(e -> e.isAfterOrEquals(bean.getRegistration().getRegistrationYear()))
                        .sorted(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR).map(x -> {
                            TupleDataSourceBean tuple = new TupleDataSourceBean();
                            tuple.setId(x.getExternalId());
                            tuple.setText(x.getName());
                            return tuple;
                        }).collect(Collectors.toList());
            }
        });
    }
}
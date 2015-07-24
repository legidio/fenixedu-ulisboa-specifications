package org.fenixedu.ulisboa.specifications.task;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;

import com.google.common.collect.Sets;

@Task(englishTitle = "Block Enrolments By Students Without Enrolments In Previous Year", readOnly = true)
public class BlockEnrolmentsByStudentsWithoutEnrolmentsInPreviousYear extends CronTask {

    private static final String BUNDLE = "resources/FenixeduUlisboaSpecificationsResources";
    private static final String LOCALE = CoreConfiguration.getConfiguration().defaultLocale();

    @Override
    public void runTask() throws Exception {

        final ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
        final ExecutionYear currentExecutionYear = currentExecutionSemester.getExecutionYear();
        final ExecutionYear previousExecutionYear = currentExecutionYear.getPreviousExecutionYear();

        if (!currentExecutionSemester.isFirstOfYear()) {
            taskLog("Nothing to be done, currently not at the beggining of a given year");
            return;

        } else {
            taskLog("Inspecting Registrations' activity in years %s and %s", previousExecutionYear.getQualifiedName(),
                    currentExecutionYear.getQualifiedName());
        }

        final Set<String> blocked = Sets.newHashSet();
        final Set<String> failed = Sets.newHashSet();
        for (final Registration registration : Bennu.getInstance().getRegistrationsSet()) {

            final String registrationInfo = registration.getNumber() + "-" + registration.getDegree().getCode();

            if (!registration.isActive()) {

                getLogger().debug("Ignoring Registration [{}]: not active", registrationInfo);
                continue;
            }

            final ExecutionYear startExecutionYear = registration.getStartExecutionYear();
            if (!startExecutionYear.isBefore(currentExecutionYear)) {

                getLogger().debug("Ignoring Registration [{}]: start execution year is {}", registrationInfo,
                        startExecutionYear.getQualifiedName());
                continue;
            }

            final RegistrationState state = getActiveStateCreatedInExecutionYear(registration, currentExecutionYear);
            if (state != null) {

                getLogger().warn("Ignoring Registration [{}]: has active {} state for {}", registrationInfo,
                        state.getStateType().getName(), startExecutionYear.getQualifiedName());
                continue;
            }

            boolean block = false;
            String reasons = StringUtils.EMPTY;

            if (registration.getEnrolments(currentExecutionYear).isEmpty()) {
                block = true;

                final String reason =
                        BundleUtil.getString(BUNDLE, LOCALE, "label.blockEnrolments.noEnrolmentsFound",
                                currentExecutionYear.getQualifiedName());

                reasons += "\n" + reason;
                getLogger().debug("Will block Registration [{}]: {}", registrationInfo, reason);
            }

            if (registration.getEnrolments(previousExecutionYear).isEmpty()) {
                block = true;

                final String reason =
                        BundleUtil.getString(BUNDLE, LOCALE, "label.blockEnrolments.noEnrolmentsFound",
                                previousExecutionYear.getQualifiedName());

                reasons += "\n" + reason;
                getLogger().debug("Will block Registration [{}]: {}", registrationInfo, reason);
            }

            if (block) {

                try {

                    final RegistrationState createdState =
                            RegistrationState.createRegistrationState(registration, (Person) null, new DateTime(),
                                    RegistrationStateType.INTERRUPTED);
                    createdState.setRemarks(String.format("Estado criado automaticamente.%s", reasons));

                    blocked.add(registrationInfo);
                    getLogger().info("Blocking enrolments for Registration [{}]", registrationInfo);

                } catch (final Throwable t) {

                    failed.add(registrationInfo);
                    getLogger().error("Error blocking Registration [{}]: {} , {} ", registrationInfo,
                            t.getClass().getSimpleName(), t.getMessage());
                }
            }
        }

        taskLog("Blocked {} Registrations", blocked.size());
        taskLog("Failed blocking {} Registrations", failed.size());
    }

    static private RegistrationState getActiveStateCreatedInExecutionYear(final Registration registration,
            final ExecutionYear executionYear) {

        final RegistrationState state = registration.getLastRegistrationState(executionYear);
        if (state != null && state.isActive() && state.getExecutionYear() == executionYear) {

            return state;

        } else {
            return null;
        }
    }

}
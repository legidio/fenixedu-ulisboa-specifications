<%@ page isELIgnored="true"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.search.competenceCourses" bundle="BOLONHA_MANAGER_RESOURCES" /></h2>
<p>&nbsp;</p>	
<fr:edit id="searchBean" name="searchBean" action="/degreeStructure/searchCompetenceCourse.do?method=search">
	<fr:schema bundle="BOLONHA_MANAGER_RESOURCES" type="org.fenixedu.ulisboa.specifications.ui.degreeStructure.SearchCompetenceCoursesDA$SearchCompetenceCourseBean">
		<fr:slot name="competenceCourse" key="competenceCourse" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="format" value="${code} - ${name}" />
			<fr:property name="indicatorShown" value="true"/>
			<fr:property name="provider" value="org.fenixedu.ulisboa.specifications.ui.degreeStructure.SearchCompetenceCoursesDA$SearchCompenceCourseProvider"/>
			<fr:property name="minChars" value="3"/>
	   </fr:slot>
	</fr:schema>
</fr:edit>

<logic:messagesPresent message="true" property="error">
	<div class="alert alert-danger alert-dismissible" role="alert">
	  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	  <html:messages id="messages" message="true" bundle="BOLONHA_MANAGER_RESOURCES" property="error"><bean:write name="messages" /></html:messages>
	</div>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="success">
	<div class="alert alert-success alert-dismissible" role="alert">
	  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	  <html:messages id="messages" message="true" bundle="BOLONHA_MANAGER_RESOURCES" property="success"><bean:write name="messages" /></html:messages>
	</div>
</logic:messagesPresent>

<c:if test="${not empty searchBean.competenceCourse}">
	<c:set value="${searchBean.competenceCourse}" var="competenceCourse" />
	
	<h2 class="mtop2">
		<c:out value="${competenceCourse.name}"/>
		</span> <small><c:out value="${competenceCourse.code}"/></small>
		<span class="badge"><c:out value="${competenceCourse.curricularStage.localizedName}"/></span>
	</h2>
	<p>
		<c:forEach items="${competenceCourse.competenceCourseInformationsSet}" var="competenceCourseInformation">
			<bean:define id="editId" name="competenceCourseInformation" property="externalId" type="java.lang.String" />
			<a data-toggle="collapse" href="<c:out value='#area-${competenceCourseInformation.externalId}' />" aria-expanded="false" aria-controls="collapseExample">
			  <c:out value="${competenceCourseInformation.executionPeriod.qualifiedName}"/> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
			</a>
			<div class="collapse" id="area-<%= editId %>">
			  <div class="well col-sm-3">
					<fr:edit action="/degreeStructure/searchCompetenceCourse.do?method=editCompetenceCourseInformation" id='<%= editId %>' name="competenceCourseInformation">
						<fr:schema type="org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation" bundle="APPLICATION_RESOURCES">
							<fr:slot name="executionPeriod" layout="menu-select" key="label.executionSemester" required="true">
								<fr:property name="format" value="${qualifiedName}"/>
								<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionSemestersProvider"/>
								<fr:property name="saveOptions" value="true"/>
							</fr:slot>				
						</fr:schema>
						<fr:layout name="flow"><fr:property name="labelExcluded" value="true"/></fr:layout>
					</fr:edit>
			  </div>
			</div>
			<div class="clearfix"></div>
		</c:forEach>
	</p>
	
<div class="btn-group" role="group" aria-label="...">
	<html:link target="_blank" styleClass="btn btn-default" page="/competenceCourses/editCompetenceCourseMainPage.faces?action=ccm&"
		paramId="competenceCourseID" paramName="competenceCourse" paramProperty="externalId">
		<bean:message key="button.edit" bundle="APPLICATION_RESOURCES" />
	</html:link>
	<c:if test="${competenceCourse.curricularStage.name == 'PUBLISHED'}">
		<html:link styleClass="btn btn-default" page="/degreeStructure/searchCompetenceCourse.do?method=approveCompetenceCourse"
			paramId="competenceCourseID" paramName="competenceCourse" paramProperty="externalId">
			<bean:message key="label.approve" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:link>
	</c:if>
</div>

<p>&nbsp;</p>	

	<c:forEach items="${competenceCourse.associatedCurricularCoursesSet}" var="curricularCourse">
		<c:set var="dcp" value="${curricularCourse.degreeCurricularPlan}" />
		<c:set var="curricularYearId" value="" />
		<h4 class="mtop2">
			<strong>
				<c:out value="[${dcp.degree.code}] ${dcp.presentationName}"/> <span class="badge"><c:out value="${dcp.state.name}"/></span>
			</strong>
			<c:forEach items="${curricularCourse.parentContextsSet}" var="context">
				<br/>
				<small>
					<c:if test="${empty context.endExecutionPeriod}"><strong></c:if>
					<c:out value="${context.parentCourseGroup.oneFullName} (${context.curricularPeriod.fullLabel})"/>
					<c:out value="${context.beginExecutionPeriod.qualifiedName}" /> <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span> 
					<c:if test="${not empty  context.endExecutionPeriod}"> <c:out value="${context.endExecutionPeriod.qualifiedName}" /></c:if>
					<c:if test="${empty context.endExecutionPeriod}"> ... </strong></c:if>
				</small>
				<bean:define id="curricularYear" name="context" property="curricularYear" />
				<c:set var="curricularYearId"><%= org.fenixedu.academic.domain.CurricularYear.readByYear(Integer.valueOf(curricularYear.toString())).getExternalId() %></c:set>
			</c:forEach>			
		</h4>
		
		<c:set var="lastExecutionYear" value="${dcp.lastExecutionYear}" />
		<c:if test="${not empty lastExecutionYear}">
			<c:forEach items="${dcp.executionDegreesSet}" var="ed">
				<c:if test="${ed.executionYear == lastExecutionYear}">
				
					<div class="panel panel-default">
					  <div class="panel-heading">
					    <h3 class="panel-title"><c:out value="${lastExecutionYear.qualifiedName}"/></h3>
					  </div>
					  <c:set var="oneExecutionCourseFound" value="false" />
						<c:forEach items="${curricularCourse.associatedExecutionCourses}" var="ec">
							<c:if test="${ec.executionPeriod.executionYear == lastExecutionYear}">
								<c:set var="oneExecutionCourseFound" value="true" />
							  <div class="panel-body">
								<h5>
									<c:out value="${ec.sigla} - ${ec.executionPeriod.qualifiedName}"/>
									<small><c:out value="${ec.degreePresentationString}"/></small>
									<bean:define id="ecLink">
										<c:out value="/editExecutionCourse.do?executionDegree=${ed.presentationName}~${ed.externalId}&executionPeriod=${ec.executionPeriod.qualifiedName}~${ec.executionPeriod.externalId}&curYear=1~${curricularYearId}&executionCoursesNotLinked=null&executionCourseId=${ec.externalId}&method=editExecutionCourse" />
									</bean:define>								
									<html:link styleClass="btn btn-link btn-xs" target="blank" module="/academicAdministration" action="<%= ecLink %>">
										<bean:message key="button.edit" bundle="APPLICATION_RESOURCES" />
									</html:link>									
								</h5>
								<h5 class="text-muted"><bean:message key="label.shifts" bundle="APPLICATION_RESOURCES" />:</h5>
								<c:forEach items="${ec.associatedShifts}" var="shift">
									<bean:define id="shift" name="shift" type="org.fenixedu.academic.domain.Shift"/>
									<bean:define id="shiftLink"><c:out value="/manageShift.do?method=prepareEditShift&page=0&shift_oid=${shift.externalId}&execution_course_oid=${ec.externalId}&academicInterval=${ec.executionPeriod.academicInterval.academicCalendarEntry.externalId}_${ec.executionPeriod.academicInterval.academicCalendar.externalId}&curricular_year_oid=${curricularYearId}&execution_degree_oid=${ed.externalId}" /></bean:define>								
									<html:link styleClass="btn btn-default btn-xs" target="blank" module="/resourceAllocationManager" action="<%= shiftLink %>">
										<span class="" data-toggle="tooltip" data-placement="top" title="<c:out value="${shift.presentationName}"/>">
											<c:out value="${shift.nome}"/>
											(<%= shift.getStudentsSet().size() %> / <c:out value="${shift.lotacao}"/>)
										</span>
									</html:link>
								</c:forEach>
								<h5 class="mtop2 text-muted"><bean:message key="title.teachers" bundle="APPLICATION_RESOURCES" />:</h5>
								<c:forEach items="${ec.professorships}" var="professorship">
									<c:out value="${professorship.person.name}" />
									<c:if test="${professorship.responsibleFor}"><span class="badge"><bean:message key="label.responsible" bundle="APPLICATION_RESOURCES" /></span></c:if><br/>
								</c:forEach>
							  </div>
							</c:if>
						</c:forEach>
						<c:if test="${not oneExecutionCourseFound}">
						 	<div class="panel-body"><span  class="text-warning"><bean:message key="log.label.noResults" bundle="APPLICATION_RESOURCES" /></span></div>
						</c:if> 
					</div>					
				</c:if>
			</c:forEach>	
		</c:if>
		
		<c:set var="previousExecutionYear" value="${lastExecutionYear.previousExecutionYear}" />
		<c:if test="${not empty previousExecutionYear}">
			<c:forEach items="${dcp.executionDegreesSet}" var="ed">
				<c:if test="${ed.executionYear == previousExecutionYear}">

					<div class="panel panel-default">
					  <div class="panel-heading">
					    <h3 class="panel-title"><c:out value="${previousExecutionYear.qualifiedName}"/></h3>
					  </div>
					  <c:set var="oneExecutionCourseFound" value="false" />
						<c:forEach items="${curricularCourse.associatedExecutionCourses}" var="ec">
							<c:if test="${ec.executionPeriod.executionYear == previousExecutionYear}">
								<c:set var="oneExecutionCourseFound" value="true" />
							  <div class="panel-body">
								<h5>
									<c:out value="${ec.sigla} - ${ec.executionPeriod.qualifiedName}"/>
									<small><c:out value="${ec.degreePresentationString}"/></small>
									<bean:define id="ecLink">
										<c:out value="/editExecutionCourse.do?executionDegree=${ed.presentationName}~${ed.externalId}&executionPeriod=${ec.executionPeriod.qualifiedName}~${ec.executionPeriod.externalId}&curYear=1~${curricularYearId}&executionCoursesNotLinked=null&executionCourseId=${ec.externalId}&method=editExecutionCourse" />
									</bean:define>								
									<html:link styleClass="btn btn-link btn-xs" target="blank" module="/academicAdministration" action="<%= ecLink %>">
										<bean:message key="button.edit" bundle="APPLICATION_RESOURCES" />
									</html:link>									
								</h5>
								<h5 class="text-muted"><bean:message key="label.shifts" bundle="APPLICATION_RESOURCES" />:</h5>
								<c:forEach items="${ec.associatedShifts}" var="shift">
									<bean:define id="shift" name="shift" type="org.fenixedu.academic.domain.Shift"/>
									<bean:define id="shiftLink"><c:out value="/manageShift.do?method=prepareEditShift&page=0&shift_oid=${shift.externalId}&execution_course_oid=${ec.externalId}&academicInterval=${ec.executionPeriod.academicInterval.academicCalendarEntry.externalId}_${ec.executionPeriod.academicInterval.academicCalendar.externalId}&curricular_year_oid=${curricularYearId}&execution_degree_oid=${ed.externalId}" /></bean:define>								
									<html:link styleClass="btn btn-default btn-xs" target="blank" module="/resourceAllocationManager" action="<%= shiftLink %>">
										<span class="" data-toggle="tooltip" data-placement="top" title="<c:out value="${shift.presentationName}"/>">
											<c:out value="${shift.nome}"/>
											(<%= shift.getStudentsSet().size() %> / <c:out value="${shift.lotacao}"/>)
										</span>
									</html:link>
								</c:forEach>
								<h5 class="mtop2 text-muted"><bean:message key="title.teachers" bundle="APPLICATION_RESOURCES" />:</h5>
								<c:forEach items="${ec.professorships}" var="professorship">
									<c:out value="${professorship.person.name}" />
									<c:if test="${professorship.responsibleFor}"><span class="badge"><bean:message key="label.responsible" bundle="APPLICATION_RESOURCES" /></span></c:if><br/>
								</c:forEach>								
							  </div>
							</c:if>
						</c:forEach>
						<c:if test="${not oneExecutionCourseFound}">
						 	<div class="panel-body"><span  class="text-warning"><bean:message key="log.label.noResults" bundle="APPLICATION_RESOURCES" /></span></div>
						</c:if> 
					</div>	

				</c:if>
			</c:forEach>		
		</c:if>			

	</c:forEach>
	
	
</c:if>


<script>
$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();
});
</script>
<%@page import="org.fenixedu.ulisboa.specifications.ui.evaluation.managemarksheet.teacher.CompetenceCourseMarkSheetController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

${portal.angularToolkit()}

<link href="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/css/dataTables.responsive.css"
	rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/omnis.js"></script>

<script src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>


<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.evaluation.manageMarkSheet.searchCompetenceCourseMarkSheet" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}<%=CompetenceCourseMarkSheetController.create"><spring:message
			code="label.event.create" /></a> |&nbsp;&nbsp;
</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>

	</div>
</c:if>





<c:choose>
	<c:when test="${not empty searchcompetencecoursemarksheetResultsDataSet}">
		<table id="searchcompetencecoursemarksheetTable" class="table responsive table-bordered table-hover" width="100%">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
					<th><spring:message code="label.CompetenceCourseMarkSheet.codeAndName" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.evaluationSeason" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.evaluationDate" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.certifier" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.shifts" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.creationDate" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.creator" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.state" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.stateDate" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.printed" /></th>
					<th><spring:message code="label.CompetenceCourseMarkSheet.rectified" /></th>
					<%-- Operations Column --%>
					<th></th>
				</tr>
			</thead>
			<tbody>

			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<div class="alert alert-warning" role="alert">

			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
				<spring:message code="label.noResultsFound" />
			</p>

		</div>

	</c:otherwise>
</c:choose>

<script>
	var searchcompetencecoursemarksheetDataSet = [
			<c:forEach items="${searchcompetencecoursemarksheetResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"
		DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"codeandname" : "<c:out value='${searchResult.codeAndName}'/>",
"evaluationseason" : "<c:out value='${searchResult.evaluationSeason}'/>",
"evaluationdate" : "<c:out value='${searchResult.evaluationDate}'/>",
"certifier" : "<c:out value='${searchResult.certifier}'/>",
"shifts" : "<c:out value='${searchResult.shifts}'/>",
"creationdate" : "<c:out value='${searchResult.creationDate}'/>",
"creator" : "<c:out value='${searchResult.creator}'/>",
"state" : "<c:out value='${searchResult.state}'/>",
"statedate" : "<c:out value='${searchResult.stateDate}'/>",
"printed" : "<c:if test="${searchResult.printed}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.printed}"><spring:message code="label.false" /></c:if>",
"rectified" : "<c:out value='${searchResult.rectified}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}<%=CompetenceCourseMarkSheetController.SEARCH_TO_VIEW_ACTION_URL%>${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
		]; $(document).ready(function() { var table = $('#searchcompetencecoursemarksheetTable').DataTable({language : { url :
		"${datatablesI18NUrl}", }, "columns": [ { data: 'codeandname' }, { data: 'evaluationseason' }, { data: 'evaluationdate' }, {
		data: 'certifier' }, { data: 'shifts' }, { data: 'creationdate' }, { data: 'creator' }, { data: 'state' }, { data: 'statedate'
		}, { data: 'printed' }, { data: 'rectified' }, { data: 'actions', className:'all' } ], //CHANGE_ME adjust the actions column
		width if needed "columnDefs": [ //54 { "width": "54px", "targets": 11 } ], "data" : searchcompetencecoursemarksheetDataSet,
		//Documentation: https://datatables.net/reference/option/dom "dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox =
		YES && ExportOptions = YES //"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES //"dom":
		'<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO //"dom": '<"col-sm-6"l>rtip', // FilterBox = NO &&
		ExportOptions = NO "tableTools": { "sSwfPath":
		"${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf" } });
		table.columns.adjust().draw(); $('#searchcompetencecoursemarksheetTable tbody').on( 'click', 'tr', function () {
		$(this).toggleClass('selected'); } ); }); </script>
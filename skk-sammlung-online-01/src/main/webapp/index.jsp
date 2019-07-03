<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:bundle basename="text">
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css' />" >
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.0/css/all.css" integrity="sha384-aOkxzJ5uQz7WBObEZcHvV5JvRW3TUc2rNPA7pe3AwnsUohiw1Vj2Rgx2KSOkF5+h" crossorigin="anonymous">
	<link rel="stylesheet" href="<c:url value='/css/default.css'/>">
	
  <title>ExpoDB für die Sammlung Online der Staatlichen Kunsthalle Karlsruhe</title>
  </head>
	<body>
		<div class="container">
			<div class="row justify-content-center">
			<div class="col-lg-8 col-md-6 col-sm-8 col-xs-12">
			<c:set var="first" value="${empty param.fst ? 0 : param.fst}" />
			<c:set var="laenge" value="${empty param.len ? 10 : param.len}" />
			<h1>ExpoDB für die Sammlung Online der Staatlichen Kunsthalle Karlsruhe</h1>
			<h2></h2>
			<form method="GET" action="<c:url value='/liste' />">
				<fieldset>
					<legend>Eingabe einer Recherche</legend>
					<c:set var="ops" value="all,any,adj,eq,le,gt" />
					<c:set var="vks" value="and,or,not" />					
					<table>
						<tr>
							<td></td>
							<td>
								<select name="index1">
									<c:forTokens items="${initParam.indexfelder}" var="idx" delims=",">
										<option value="${idx}" <c:if test='${idx eq param.index1}'>selected</c:if>><fmt:message key="${idx}"/></option>							
									</c:forTokens>
								</select>
							</td>
							<td>
								<select name="op1">
									<c:forEach items="${ops}" var="op">
										<option value="${op}" <c:if test='${op eq param.op1}'>selected</c:if>><fmt:message key="${op}"/></option>							
									</c:forEach>								
								</select>
							</td>
							<td>	
								<input name="term1" type="text" value="${param.term1}">
							</td>						
						</tr>
						<tr>
							<td>
								<select name="vk2">
									<c:forEach items="${vks}" var="vk">
										<option value="${vk}" <c:if test='${vk eq param.vk2}'>selected</c:if>><fmt:message key="${vk}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td>
								<select name="index2">
									<c:forEach items="${initParam.indexfelder}" var="idx">
										<option value="${idx}" <c:if test='${idx eq param.index2}'>selected</c:if>><fmt:message key="${idx}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td>
								<select name="op2">
									<c:forEach items="${ops}" var="op">
										<option value="${op}" <c:if test='${op eq param.op2}'>selected</c:if>><fmt:message key="${op}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td>	
								<input name="term2" type="text" value="${param.term2}">
							</td>						
						</tr>
						<tr>
							<td>
								<select name="vk3">
									<c:forEach items="${vks}" var="vk">
										<option value="${vk}" <c:if test='${vk eq param.vk3}'>selected</c:if>><fmt:message key="${vk}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td>
								<select name="index3">
									<c:forEach items="${initParam.indexfelder}" var="idx">
										<option value="${idx}" <c:if test='${idx eq param.index3}'>selected</c:if>><fmt:message key="${idx}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td>
								<select name="op3">
									<c:forEach items="${ops}" var="op">
										<option value="${op}" <c:if test='${op eq param.op3}'>selected</c:if>><fmt:message key="${op}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td>	
								<input name="term3" type="text" value="${param.term3}">
							</td>						
						</tr>
						<tr>
							<td colspan="2">Sortierung: 
								<select name="srt">
									<c:forEach items="${initParam.sortierfelder}" var="srt">
										<option value="${srt}" <c:if test='${srt eq param.srt}'>selected</c:if>><fmt:message key="${srt}"/></option>							
									</c:forEach>
								</select>
							</td>
							<td colspan="2">								
								<a href="<c:url value='/index.jsp'/>" class="btn btn-primary" href="#" role="button">Zurücksetzen</a>
								<input class="btn btn-primary" type="submit" value="Senden">
							</td>
						</tr>
					</table>
				</fieldset>
			</form>
			<c:if test="${! empty reccount}">				
				<p><strong>Query-Syntax: </strong>${selektQuery} <strong>Solr-Query-String: </strong>${solrQuery}</p>					
				<div id="treffer">
					<jsp:include page="/local/pagination.jsp" />
					<div class="accordion" id="accordionExample">
						<c:forEach items="${treffer}" var="hit" varStatus="status">
						  <div class="card">
						    <div class="card-header" id="heading${status.index}">
						      <h5 class="mb-0">
						        <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapse${status.index}" aria-expanded="true" aria-controls="collapse${status.index}">
						          ${first + status.index}:
						          <c:if test="${not empty hit.images}">
									<c:url value="/image" var="img" >
										<c:param name="id" value="${hit.id}" />
									</c:url>
									<img src="${img}" height="40"/>
								  </c:if> 
						          <c:out value="${hit.header}" escapeXml="false"/>						          
						        </button>
						      </h5>
						    </div>						
						    <div id="collapse${status.index}" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
						      <div class="card-body">
						      	<table>
						      		<tr>
						      			<td><c:out value="${hit.detail}" escapeXml="false"/></td>
						      			<td>
							      			<c:if test="${not empty hit.images}">
												<c:url value="/image" var="img" >
													<c:param name="id" value="${hit.id}" />
													<c:param name="width" value="300" />
												</c:url>
												<img src="${img}" width="400" class="rounded float-right"/>
											</c:if>
						      			</td>
						      		</tr>
						      	</table>
						       </div>
						    </div>
						  </div>						 
						</c:forEach>
					</div>
					<jsp:include page="/local/pagination.jsp" />
				</div>
				<div style="margin-top: 50px;">	
					<p>
					  <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
					    Datenexporte über REST-Schnittstelle
					  </button>
					</p>
					<div class="collapse" id="collapseExample">
					  <div class="card card-body">
					    <form method="get" action="<c:url value='/export'/>">
							<fieldset>
								<legend>Export von Daten</legend>
								<input type="hidden" name="qry" value='${selektQuery}' />
								<input type="hidden" name="srt" value='${param.srt}' />
								<table>
									<tr>
										<td>
											Export-Format
										<td/>
										<td>
											Erster Datensatz
										<td/>
										<td>
											Anzahl Datensätze
										<td/>
									</tr>
									<tr>
										<td>
											<select name="fmt" style="width: 400px;">
												<c:forEach items="${templates}" var="tpl">
													<option value="${tpl.key}">${tpl.value}</option>							
												</c:forEach>							
											</select>
										<td/>
										<td>
											<input name="fst" type="number" value="${first}" size="4"/>
										<td/>
										<td>
											<input name="len" type="number" value="${laenge}" size="4" />
										<td/>
									</tr>
								</table>								 
								<input type="submit" class="btn btn-primary" value="Export">
							</fieldset>
						</form>
					  </div>
					</div>			
				</div>						
			</c:if>			
		</div>
		</div>
		</div>
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>	
	</body>
</html>
</fmt:bundle>

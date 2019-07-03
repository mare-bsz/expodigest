<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:url var="liste" value="/liste" >
	<c:forEach var='parameter' items='${paramValues}'> 
		<c:if test="${parameter.key ne 'fst'}">
			<c:param name="${parameter.key}" value="${param[parameter.key]}" /> 
		</c:if>
	</c:forEach>
</c:url>
<c:set var="first" value="${empty param.fst ? 0 : param.fst}" />
<c:set var="laenge" value="${empty param.len ? 10 : param.len}" />
<table class="swbexpo_pagination">
	<tr>
		<td class="swbexpo_pagination_fst">
			<c:if test="${first eq 0}">
				<button type="button" class="btn btn-primary" disabled><i class="fas fa-angle-double-left "></i></button>
			</c:if>
			<c:if test="${first gt 0}">
				<a class="btn btn-primary" href="${liste}&fst=0"><i class="fas fa-angle-double-left "></i></a>
			</c:if>
		</td>
		<td class="swbexpo_pagination_rev">
			<c:if test="${first eq 0}">
				<button type="button" class="btn btn-primary" disabled><i class="fas fa-angle-left "></i></button>
			</c:if>
			<c:if test="${first gt 0}">
				<a class="btn btn-primary" href="${liste}&fst=${first - 10}"><i class="fas fa-angle-left "></i></a>
			</c:if>
		</td>
		<td class="swbexpo_pagination_pages">
			${first} bis ${first + 9} von ${reccount}
		</td>
		<td class="swbexpo_pagination_ff">
			<c:if test="${not (first lt reccount - 10)}">
				<button type="button" class="btn btn-primary" disabled><i class="fas fa-angle-right"></i></button>
			</c:if>
			<c:if test="${first lt reccount - 10}">
				<a class="btn btn-primary" href="${liste}&fst=${first + 10}"><i class="fas fa-angle-right"></i></a>
			</c:if>
		</td>
		<td class="swbexpo_pagination_lst">
			<c:if test="${not (first lt reccount - 10)}">
				<button type="button" class="btn btn-primary" disabled><i class="fas fa-angle-double-right"></i></button>
			</c:if>
			<c:if test="${first lt reccount - 10}">
				<a class="btn btn-primary" href="${liste}&fst=${reccount - 9}"><i class="fas fa-angle-double-right"></i></a>
			</c:if>
		</td>								
	</tr>		
</table>
	

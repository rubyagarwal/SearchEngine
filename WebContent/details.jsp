<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Results</title>
</head>
<body style="margin-left: 120px; margin-top:50px" >
	<div>
		<img src="logo.jpg" alt="RuSearch" height="150" width="450" style ="margin-left:300px ">
	</div>
	<br/>
<form method="post" action="getServlet" id="fid">
			<table>
				<tr>
					<td>&nbsp;</td>
					<td><input size="80" type="text" name="name" value="${queryCor}"/></td>
					<td>&nbsp;</td>
					<td><input type="image" src="search.png" height="50"
						width="50" alt="RU Search" value="search" name = "page" ></td>
				</tr>
			</table>
</form>
	 <br/>
	<h2>
		Showing results for
		 <b style="color: blue"> ${query} </b>					
	</h2>
	<h3>
		<span style="color: red;font-size: 110%;">Did you mean:</span>
			<a href="#" onclick="document.getElementById('fid').submit();">
			<i style="color: blue"> ${queryCor} </i>
			</a>
			
	</h3>
	<span style="color: gray; font-weight: bold; font-size: 110%;">${info }</span>
	<br />
	<br />
	<br />
	
	<c:forEach items="${urls}" var="u">
		<c:forEach items="${u.value}" var="v">
			<%
				String i = (String) pageContext.getAttribute("v");
						i = i.replace("http://", "");
						i = i.replace("https://", "");
						i = i.replace("www.", "");
						i = i.replace(".com", "");
						i = i.replace(".org", "");
						i = i.replace(".edu", "");
						i = i.replace(".it", "");
						i = i.replace(".ac.uk", "");
						i = i.replace(".ac.jp", "");
						String[] splits = i.split("/");
						String b = "";
						for (int j = 0; (j < 3) && (j < splits.length); j++) {
							b = b.concat(splits[j].toUpperCase() + " ");

						}
						if (splits.length > 3) {
							b = b.concat(splits[splits.length - 1].toUpperCase()
									+ " ");
						}
			%>
			<span style="color: blue; font-size: 160%; font-weight: bold;">
			<a href=${v}><%=b%></a></span>
			<br />
			<span style="color: green; font-size: 120%; font-style: italic;">${v}</span>
			<br />
			<c:forEach items="${u.key}" var="k">
				<strike>${k} </strike>
			</c:forEach>
			<br />
			<br>
		</c:forEach>
		<br>
	</c:forEach>
</body>
</html>
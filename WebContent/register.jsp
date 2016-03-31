<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RU Search</title>
</head>
<body>
	<center>
		<br/> <br/> <br/>

		<div>
			<img src="logo.jpg" alt="RuSearch" height="200" width="600">
		</div>
		<br/> <br/>
		<form method="post" action="getServlet">
			<table>
				<tr>
					<td>&nbsp;</td>
					<td><input size="80" type="text" name="name" /></td>
					<td>&nbsp;</td>
					<td><input type="image" src="search.png" height="50"
						width="50" alt="RU Search" value="search" name = "page" ></td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>
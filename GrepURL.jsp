<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"  import="crawl.URL_Childs" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GREP CHILD</title>
</head>
<body>

<%  
String url=request.getParameter("url");  
int depth=Integer.parseInt(request.getParameter("depth"));
 
%>
<pre><h3>
<% out.print("Request URL:  <a href=" +url +">"+url+"</a>  |  Depth: "+depth); %>

</h3></pre>   

<br>
<% out.print("JSON Response : "+ new URL_Childs().URL_Child_response(url,depth) ); %>
<br>
<h2><a href=MainPage.html>Try Another URL</a></h2>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connect to IB API</title>
</head>
<body style="background: black; color: white; align-content: center">
<div style="
    position: absolute;
    left: 40%;
    top: 20%;
    display: inline-flex;
    flex-direction: column;
    align-items: center">
    <img src="static/ib-logo-360x360.png" alt="ibkr-logo">
    <h2>IBKR Dashboard app</h2>
    <p>Press on the button below to start fetching of the data and<br>
        wait 10s to be redirected to the page with fetched positions</p>
    <h3>
        <form method="get" action="${pageContext.servletContext.contextPath}/app-servlet" target="_blank">
            <button type="submit" onclick="alert('Do you want to connect to IB Gateway? Press OK...')"
                    value="Submit">Connect to IB API
            </button>
        </form>
    </h3>
</div>
</body>
</html>
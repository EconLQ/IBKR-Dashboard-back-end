<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Positions Dashboard</title>
    <link rel=“stylesheet” type=“text/css” href="${pageContext.request.contextPath}/static/css/positions.css">
    <style>
        table, th, td {
            width: -webkit-fill-available;
            text-align: center;
            margin: auto;
            border: 1px solid white;
            border-collapse: collapse;
        }
    </style>
</head>
<body style="background-color: black; color: white">
<h2>All positions in the current portfolio</h2>
<p><i>*Note:</i>If you refresh the page it'll fetch new (if any) positions from the TWS. It may take up to 10s</p>
<table class="positions-table">
    <thead>
    <tr>
        <th>Contract Id</th>
        <th>Ticker</th>
        <th>Entry date</th>
        <th>Size</th>
        <th>Unrealized PnL</th>
        <th>Realized PnL</th>
        <th>Avg. Cost</th>
        <th>Last price</th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="positions" scope="request" type="java.util.List"/>
    <c:forEach var="position" items="${positions}">
        <tr>
            <td><c:out value="${position.contractId}"/></td>
            <td><c:out value="${position.ticker}"/></td>
            <td><c:out value="${position.date}"/></td>
            <td><c:out value="${position.averageCost}"/></td>
            <td><c:out value="${position.unrealizedPnL}"/></td>
            <td><c:out value="${position.realizedPnL}"/></td>
            <td><c:out value="${position.averageCost}"/></td>
            <td><c:out value="${position.lastMarketPrice}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

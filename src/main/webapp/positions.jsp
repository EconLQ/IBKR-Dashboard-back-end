<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en-US"/>
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
            <td><c:out value="${position.position}"/></td>
            <td><c:out value="${position.unrealizedPnL}"/></td>
            <td><c:out value="${position.realizedPnL}"/></td>
            <td><c:out value="${position.averageCost}"/></td>
            <td><c:out value="${position.lastMarketPrice}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<p>
    Portfolio Net Liquidation Value (The basis for determining the price of the assets in your account. <br>Total cash
    value + stock value + options value + bond value):
    <span style="font-weight: bold; font-style: italic" id="portfolioNetLiq"></span>
</p>
<script>
    // get portfolio net liquidation value from request param
    const num = ${portfolioNetLiquidation};
    document.getElementById("portfolioNetLiq").innerHTML = num.toLocaleString("en-US");
</script>
<p>
    Portfolio Daily Unrealized PnL:
    <c:choose>
        <c:when test="${portfolioUnrealizedPnL >= 0}">
            <span style="color: green"><b><c:out value="${portfolioUnrealizedPnL}"/></b></span>
        </c:when>
        <c:when test="${portfolioUnrealizedPnL < 0}">
            <span style="color: red"><b><c:out value="${portfolioUnrealizedPnL}"/></b></span>
        </c:when>
    </c:choose>
</p>
<p>
    Portfolio Daily Realized PnL :
    <c:choose>
        <c:when test="${portfolioRealizedPnL >= 0}">
            <span style="color: green"><b><c:out value="${portfolioRealizedPnL}"/></b></span>
        </c:when>
        <c:when test="${portfolioRealizedPnL < 0}">
            <span style="color: red"><b><c:out value="${portfolioRealizedPnL}"/></b></span>
        </c:when>
    </c:choose>
</p>
</body>
</html>

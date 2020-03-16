<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>订单详细</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
	* {
		font-size: 11pt;
	}
	div {
		border: solid 2px gray;
		width: 75px;
		height: 75px;
		text-align: center;
	}
	li {
		margin: 10px;
	}
	
	#pay {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -412px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
	#pay:HOVER {
		background: url(<c:url value='/images/all.png'/>) no-repeat;
		display: inline-block;
		
		background-position: 0 -448px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
</style>
  </head>
  
  <body>
<h1>当前订单</h1>

<table border="1" width="100%" cellspacing="0" background="black">
	<tr bgcolor="gray" bordercolor="gray">
		<td colspan="6">
			订单编号：${order.oid }　成交时间：${order.ordertime }　金额：<font color="red"><b>${order.total }元</b></font>
		</td>
	</tr>
	<!-- 订单项 -->
	<c:forEach var="orderItem" items="${order.orderItems }">
		<tr bordercolor="gray" align="center">
			<td width="15%">
				<div><img src="${pageContext.request.contextPath }/${orderItem.book.image}" width="65" height="75"/></div>
			</td>
			<td>书名：${orderItem.book.bname }</td>
			<td>单价：${orderItem.book.price }元</td>
			<td>作者：${orderItem.book.author }</td>
			<td>数量：${orderItem.count }</td>
			<td>小计：${orderItem.subtotal }元</td>
		</tr>
	</c:forEach>
</table>
<br/>
		<!-- 向易宝出发 -->
		<form method="post" action="${pageContext.request.contextPath }/order" id="form" target="_parent">
			<input type="hidden" name="method" value="payOrder"/>
			<input type="hidden" name="oid" value="${order.oid }"/>
			收货地址：<input type="text" name="address" size="50" value="江西省南昌市顶峰大厦2楼216室xxx收"/><br/>
		
			选择银行：<br/>
			<input type="radio" name="pd_FrpId" value="ICBC-NET-B2C" checked="checked"/>工商银行
			<img src="${pageContext.request.contextPath }/bank_img/icbc.bmp" align="middle"/>
			<input type="radio" name="pd_FrpId" value="BOC-NET-B2C"/>中国银行
			<img src="${pageContext.request.contextPath }/bank_img/bc.bmp" align="middle"/><br/><br/>
			<input type="radio" name="pd_FrpId" value="ABC-NET-B2C"/>农业银行
			<img src="${pageContext.request.contextPath }/bank_img/abc.bmp" align="middle"/>
			<input type="radio" name="pd_FrpId" value="CCB-NET-B2C"/>建设银行
			<img src="${pageContext.request.contextPath }/bank_img/ccb.bmp" align="middle"/><br/><br/>
			<input type="radio" name="pd_FrpId" value="BOCO-NET-B2C"/>交通银行
			<img src="${pageContext.request.contextPath }/bank_img/bcc.bmp" align="middle"/>
			<input type="radio" name="pd_FrpId" value="CMBCHINA-NET-B2C"/>招商银行
			<img src="${pageContext.request.contextPath }/bank_img/cmb.bmp" align="middle"/><br/>
		
		</form>
<a id="pay" href="javascript:document.getElementById('form').submit();"></a>

  </body>
</html>


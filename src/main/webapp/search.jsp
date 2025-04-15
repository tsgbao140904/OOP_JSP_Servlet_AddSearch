<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Kết quả tìm kiếm</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 10px 20px;
            background-color: white;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .user-list {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        .user-card {
            background-color: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            text-decoration: none;
            color: #000;
        }
        .form-control {
            width: 200px;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .btn-primary {
            background-color: #1877f2;
            color: white;
        }
        .btn-primary:hover {
            background-color: #166fe5;
        }
        .no-results-img {
            max-width: 300px;
            margin: 20px auto;
            display: block;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>Kết quả tìm kiếm</h1>
    <div class="user-info">
        <form action="${pageContext.request.contextPath}/search" method="get">
            <input type="text" name="query" placeholder="Tìm kiếm người dùng..." value="${query}" class="form-control" style="display: inline-block;">
            <button type="submit" class="btn btn-primary" style="margin-left: 5px;">Tìm</button>
        </form>
        <a href="${pageContext.request.contextPath}/home" class="btn">Trang chủ</a>
        <c:if test="${sessionScope.user != null}">
            <span>Xin chào, ${sessionScope.user.username}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn">Đăng xuất</a>
        </c:if>
    </div>
</div>
<div class="container">
    <p>Debug: users = ${users}, query = ${query}</p>
    <c:choose>
        <c:when test="${users != null && !users.isEmpty()}">
            <h2>Kết quả tìm kiếm cho "${query}"</h2>
            <div class="user-list">
                <c:forEach items="${users}" var="user">
                    <div class="user-card">
                        <span>${user.username}</span>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:when test="${users != null}">
            <h2>Không tìm thấy người dùng nào cho "${query}"</h2>
            <img src="${pageContext.request.contextPath}/img/no_results.png" alt="No results" class="no-results-img">
        </c:when>
        <c:otherwise>
            <h2>Vui lòng nhập từ khóa để tìm kiếm</h2>
            <img src="${pageContext.request.contextPath}/img/no_results.png" alt="No results" class="no-results-img">
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
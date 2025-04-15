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
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .btn-follow {
            padding: 4px 8px;
            background-color: #1877f2;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9em;
        }
        .btn-follow.following {
            background-color: #e4e6eb;
            color: #050505;
        }
        .btn-follow:hover {
            opacity: 0.9;
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
        <a href="${pageContext.request.contextPath}/follows/following" class="btn" style="margin-right: 10px;">
            <i class="fas fa-users"></i> Theo dõi
        </a>
        <span>Xin chào, ${sessionScope.user.username}</span>
        <a href="${pageContext.request.contextPath}/logout" class="btn">Đăng xuất</a>
    </div>
</div>

<div class="container">
    <c:choose>
        <c:when test="${users != null && !users.isEmpty()}">
            <h2>Kết quả tìm kiếm cho "${query}"</h2>
            <div class="user-list">
                <c:forEach items="${users}" var="user">
                    <div class="user-card">
                        <span>${user.username}</span>
                        <c:if test="${sessionScope.user.id != user.id}">
                            <button onclick="toggleFollow('${user.id}')"
                                    class="btn-follow ${user.followedByCurrentUser ? 'following' : ''}"
                                    id="follow-btn-${user.id}">
                                <c:choose>
                                    <c:when test="${user.followedByCurrentUser}">
                                        <i class="fas fa-user-minus"></i> Bỏ theo dõi
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-user-plus"></i> Theo dõi
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:when test="${users != null}">
            <h2>Không tìm thấy người dùng nào cho "${query}"</h2>
        </c:when>
        <c:otherwise>
            <h2>Vui lòng nhập từ khóa để tìm kiếm</h2>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function toggleFollow(userId) {
        const buttonId = 'follow-btn-' + userId;
        const followButton = document.getElementById(buttonId);
        const isFollowing = followButton.classList.contains('following');
        const method = isFollowing ? 'DELETE' : 'POST';
        const baseUrl = '${pageContext.request.contextPath}';
        const url = baseUrl + "/follow/" + userId;

        fetch(url, {
            method: method
        })
            .then(response => {
                if (response.ok) {
                    followButton.classList.toggle('following');
                    if (isFollowing) {
                        followButton.innerHTML = '<i class="fas fa-user-plus"></i> Theo dõi';
                    } else {
                        followButton.innerHTML = '<i class="fas fa-user-minus"></i> Bỏ theo dõi';
                    }
                } else {
                    alert('Có lỗi xảy ra khi thực hiện thao tác này');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi thực hiện thao tác này');
            });
    }
</script>
</body>
</html>
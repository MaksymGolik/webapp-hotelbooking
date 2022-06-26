<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/bootstrap-5.0.2-dist/css/bootstrap.css" rel="stylesheet" type="text/css">
    <!-- Font Icon -->
    <link rel="stylesheet" href="/fonts/material-icon/css/material-design-iconic-font.min.css">

    <!-- Main css -->
    <link rel="stylesheet" href="/css/styleAuth.css">
</head>
<body>
    <div class="main">
        <section class="sign-in">
            <div class="container">
                <div class="signin-content">
                    <div class="signin-image">
                        <figure><img src="/images/signin-pic.jpg" alt="sing up image"></figure>
                        <a href="register" class="signup-image-link">Create an account</a>
                    </div>

                    <div class="signin-form">
                        <h2 class="form-title">Sign in</h2>
                        <form method="POST" class="register-form" id="login-form">
                            <div class="form-group">
                                <label for="email"><i class="zmdi zmdi-email"></i></label>
                                <input type="text" pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$" maxlength="40" required name="email" id="email" placeholder="Your Email"/>
                            </div>
                            <div class="form-group">
                                <label for="pass"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" minlength="6" maxlength="16"  required name="password" id="pass" placeholder="Password"/>
                            </div>
                            <div class="form-group form-button">
                                <input type="submit" name="signin" id="signin" class="form-submit" value="Log in"/>
                            </div>
                        </form>
                        ${sessionScope.errorMessage}
                        <c:remove var="errorMessage" scope="session"/>
                    </div>
                </div>
            </div>
        </section>
    </div>

</body>
</html>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Liked Profiles</title>
</head>
<body class="container mt-4">
    <h2>Profiles you liked:</h2>
    <div class="list-group">
        <#list profiles as profile>
            <a href="/messages/${profile.id}" class="list-group-item list-group-item-action d-flex align-items-center">
                <img src="${profile.photoUrl}" alt="${profile.name}" style="width: 50px; height: 50px; border-radius: 50%; margin-right: 15px;">
                ${profile.name}
            </a>
        <#else>
            <p>You haven't liked anyone yet.</p>
        </#list>
    </div>
    <a href="/users" class="btn btn-primary mt-3">Back to swiping</a>
</body>
</html>
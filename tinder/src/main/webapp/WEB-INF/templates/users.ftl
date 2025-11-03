<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Tinder</title>
</head>
<body class="d-flex justify-content-center align-items-center vh-100">
    <div class="card" style="width: 25rem;">
      <img src="${profile.photoUrl}" class="card-img-top" alt="${profile.name}">
      <div class="card-body text-center">
        <h5 class="card-title">${profile.name}</h5>

        <form method="POST" action="/users" class="d-flex justify-content-around">
            <input type="hidden" name="profileId" value="${profile.id}">
            <button type="submit" name="choice" value="no" class="btn btn-danger btn-lg">No</button>
            <button type="submit" name="choice" value="yes" class="btn btn-success btn-lg">Yes</button>
        </form>
      </div>
    </div>
</body>
</html>
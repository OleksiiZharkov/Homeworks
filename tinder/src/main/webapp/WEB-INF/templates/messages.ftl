<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Chat with ${partner.name}</title>
    <style>
        .chat-container { max-height: 60vh; overflow-y: auto; }
        .msg-sent { text-align: right; }
        .msg-sent .alert { display: inline-block; background-color: #d1e7dd; }
        .msg-received .alert { display: inline-block; background-color: #f8f9fa; }
    </style>
</head>
<body class="container mt-4">
    <div class="card">
        <div class="card-header d-flex align-items-center">
            <img src="${partner.photoUrl}" alt="${partner.name}" style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px;">
            <h5 class="mb-0">Chat with ${partner.name}</h5>
            <a href="/liked" class="btn btn-secondary ms-auto">Back</a>
        </div>

        <div class="card-body chat-container p-3" id="chat-box">
            <#list messages as msg>
                <#if msg.senderId == currentUserId>
                    <div class="msg-sent mb-2">
                        <div class="alert alert-light" role="alert">
                            ${msg.content}
                        </div>
                    </div>
                <#else>
                    <div class="msg-received mb-2">
                         <div class="alert alert-secondary" role="alert">
                            ${msg.content}
                        </div>
                    </div>
                </#if>
            </#list>
        </div>

        <div class="card-footer">
            <form method="POST" action="/messages/${partner.id}" class="d-flex">
                <input type="text" name="message" class="form-control" placeholder="Type your message..." required>
                <button type="submit" class="btn btn-primary ms-2">Send</button>
            </form>
        </div>
    </div>
</body>
</html>
function post() {
    var questionId = $("#question_id").val();//获取问题id
    var content = $("#comment_content").val();//获取评论内容
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                //回复成功，刷新
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    //未登录
                    var isAccepted = confirm(response.message);//弹出确认框
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=d06bcd194bb01844461f&redirect_uri=" + document.location.origin + "/callback&scope=user&state=2");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}


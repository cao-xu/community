function post() {
    //插入新的评论（面向问题）
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

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id); /*二级评论的id*/

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {//已经查询完了,已拼接完了，直接展开
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {//还未查询二级评论，进行查询
            //向查询二级评论的接口发送请求，获取二级评论
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    //定义开关
    var flag = true;
    //页面输入的标签
    var value = e.getAttribute("data-tag");
    //输入框中的标签
    var previous = $("#tag").val();
    //将输入框中的标签按,分割得到标签数组
    var psplits = previous.split(",");
    //循环数组与输入的标签值进行比较
    for(var i = 0 ; i < psplits.length ; i++){
        if(psplits[i] == value){
            flag = false;
        }
    }
    //如果没有重复元素的话,再添加
    if(flag){
        $(document.getElementById('all' + value)).addClass("publish-tag-selected");//添加选中的样式标签
        if(previous){ //之前文本框不为空
            $("#tag").val(previous+','+value);//设置尾加标签
        }else{ //之前为空
            $("#tag").val(value);//设置标签文本
        }
    }
}

function deleteTag(e) {
    const removeValue = e.getAttribute("data-tag");//获取待删除的值
    const content = $("#tag").val();//获取输入框的值
    //防止字串干扰
    let arr = content.split(',');
    count = arr.length;
    if (arr.indexOf(removeValue) > -1) {
        for (let i = 0; i < arr.length; i++) {
            if (arr[i] === removeValue) {
                arr.splice(i, 1);
                $(document.getElementById('all' + removeValue)).removeClass("publish-tag-selected");
                count--;
                break;
            }
        }
        $("#tag").val(arr.join(','));
    } else {
        alert('不要再点啦，我还没被选呢');
    }
}
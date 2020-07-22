var wsmodule = {
    logined: function (msg) {
        console.log();
    }
    , im: function (msg) {
        console.log(msg);
    }
}
$(function () {
    var body = "";
    if ($("body").attr("base")) {
        body = $("body").attr("base");
    }
    $("<div/>").appendTo("body").load(body + "/protected/im_panel.shtml", function () {
        im.events.init();
    });

    $(".im_receiver_btn").each(function () {
        $(this).load(body + "/protected/im_receiver.shtml?id=" + $(this).attr("rid"));
    });
});
function screenFuc() {
    var topHeight = $(".chatBox-head").innerHeight();//聊天头部高度
    //屏幕小于768px时候,布局change
    var winWidth = $(window).innerWidth();

    if (winWidth <= 768) {
        var totalHeight = $(window).height(); //页面整体高度
        $(".chatBox-info").css("height", totalHeight - topHeight);
        var infoHeight = $(".chatBox-info").innerHeight();//聊天头部以下高度
        //中间内容高度
        $(".chatBox-content").css("height", infoHeight - 46);
        $(".chatBox-content-demo").css("height", infoHeight - 46);

        $(".chatBox-list").css("height", totalHeight - topHeight);
        $(".chatBox-kuang").css("height", totalHeight - topHeight);
        $(".div-textarea").css("width", winWidth - 106);
    } else {
        $(".chatBox-info").css("height", 448);
        var infoHeight = $(".chatBox-info").innerHeight();
        $(".chatBox-content").css("height", infoHeight - 47);
        $(".chatBox-content-demo").css("height", infoHeight - 47);
        $(".chatBox-list").css("height", 448);
        $(".chatBox-kuang").css("height", 448);
        $(".div-textarea").css("width", 260);
    }
}
function selectImg(pic) {
    if (!pic.files || !pic.files[0]) {
        return;
    }
    var reader = new FileReader();
    var $this=$(this);
    reader.onload = function (evt) {
        var images = evt.target.result;
        var body = "<img src=" + images + ">";
        var now = new Date();
        if(body.length>30*1024){
            layer.msg("图片不能超过30KB",{icon:5});
            return;
        }
        var msg = {
            time: new Date().getTime()
            , timeFmt: now.getFullYear() + "-" + (Array(2).join('0') + (now.getMonth() + 1)).slice(-2) + "-" + now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds()
            , body: body
            , type: 2//0文本消息 1表情 2图片
            , action: 1//0接收 1发送
            , read: true//是否已读
            , sender: im.vars.getLoginUser()
            , receiver: im.vars.getCurUser()
        };
        im.funs.appendMsg(msg);
        im.funs.sendMsg(msg);
        $(document).ready(function () {
            $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
        });
    };
    reader.readAsDataURL(pic.files[0]);
}

var message = {
    time: 0
    , timeFmt: ""
    , body: ""
    , type: 0//0文本消息 1表情 2图片
    , action: 0//0接收 1发送
    , read: true//是否已读
    , sender: {}
    , receiver: {}

};
var user = {
    id: ""
    , code: ""
    , name: ""
    , avatar: ""
    , status: true
    , type: 0 //0真人交流 1学习提醒 2智能答疑 3学习推荐
    , messages: []
}
var im = {
    funs: {
        //发送消息
        sendMsg: msg => {
            im.vars.websocket.send(JSON.stringify(msg));
        }
        //接收消息，可以多条数据
        , receiveMsg: msgs => {
            $.each(msgs, function () {
                if (this.type == 0 || this.type == 1 || this.type == 2) {
                    if (this.receiver.id == im.vars.loginUser.id && (!im.vars.users[this.sender.id])) {
                        im.funs.appendUser(this.sender);
                        im.vars.users[this.sender.id].messages.push(this);
                    } else if (this.receiver.id != im.vars.loginUser.id) {
                        if ((!im.vars.users[this.receiver.id])) {
                            im.funs.appendUser(this.receiver);
                        }
                        this.action = 1;
                        im.vars.users[this.receiver.id].messages.push(this);
                    } else {
                        im.vars.users[this.sender.id].messages.push(this);
                    }
                    if (im.vars.curUser.id == this.sender.id || im.vars.curUser.id == this.receiver.id) {
                        im.funs.readMsg(im.vars.curUser);
                        im.funs.appendMsg(this);
                    }
                } else if (this.type == 6) {
                    im.vars.users[this.sender.id].messages.push(this);
                    if (im.vars.curUser.id == this.sender.id || im.vars.curUser.id == this.receiver.id) {
                        im.funs.readMsg(im.vars.curUser);
                    }
                    im.funs.appendMsg(this);
                }
            });
            im.funs.allMsgNum();
        }
        //读取消息
        , readMsg: user => {
            $.post("/im/readMsg.bi", { sender: user.id, receiver: im.vars.loginUser.id }, function () { });
            $.each(im.vars.users[user.id].messages, function () {
                this.read = true;
            });
            im.funs.allMsgNum();
        }
        //显示消息
        , appendMsg: msg => {
            var msgBody = $("#message_body_template").clone().removeAttr("id").removeClass("hide");
            msgBody.appendTo("#chatBox-content-demo");
            msgBody.find(".chat-date").html(msg.timeFmt);
            msgBody.find(".chat-avatars img").attr("src", msg.sender.avatar).attr("alt", msg.sender.name);
            msgBody.find(".chat-message").html(msg.body);
            msgBody.find(".action").addClass(msg.action == 1 ? "right" : "left");
            var htmlContent;
            if(msg.type!=6){
                htmlContent = msg.body.replace(/[\n\r]/g, '<br>');
            }else{
                htmlContent = msg.body;
            }
            var body = $("<div/>").addClass("chat-message").html(htmlContent);
            if (msg.action == 1) {
                msgBody.find(".action").prepend(body);
            } else {
                msgBody.find(".action").append(body);
            }
            $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
        }

        //清除消息
        , clearMsg: () => {

        }
        , allMsgNum: () => {
            var allcnt = 0;
            $.each(im.vars.users, (idx, item) => {
                console.log(item)
                if (item.messages) {
                    var size = item.messages.filter(msg => !(msg.read)).length;
                    if (size) {
                        $("#id_" + item.id).find(".message-num").html(size);
                    } else {
                        $("#id_" + item.id).find(".message-num").html("");
                    }
                    allcnt += size;
                }
            });
            console.log(allcnt);
            if (allcnt) {
                $(".chat-message-num").html(allcnt);
            } else {
                $(".chat-message-num").html("");
            }
        }
        , appendUser: user => {
            if (!im.vars.users[user.id]) {
                user.messages = [];
                im.vars.users[user.id] = user;
                var userPanel = $("#receiver_body_template").clone().removeClass("hide").attr("id", "id_" + user.id).attr("data", user.id);
                userPanel.find("img").attr("src", user.avatar).attr("alt", user.name);
                userPanel.find("p").html(user.name);
                userPanel.appendTo("#im_user_list_panel");
            }
        }
    }
    , vars: {
        users: {}
        , loginUser: {}
        , curUser: {}
        , websocket: {}
        , getLoginUser: function () {
            return {
                id: im.vars.loginUser.id
                , name: im.vars.loginUser.name
                , code: im.vars.loginUser.code
                , avatar: im.vars.loginUser.avatar
            };
        }
        , getCurUser: function () {
            return {
                id: im.vars.curUser.id
                , name: im.vars.curUser.name
                , code: im.vars.curUser.code
                , avatar: im.vars.curUser.avatar
                , type: im.vars.curUser.type
            };
        }
    }
    , events: {
        init: function () {
            im.vars.loginUser = {
                id: $("#im_user_info #myId").val()
                , code: $("#im_user_info #myCode").val()
                , name: $("#im_user_info #myName").val()
                , avatar: $("#im_user_info #myAvatar").val()
            }
            if ($("#myIden").val() == "Student") {
                im.funs.appendUser({
                    id: "remind"
                    , code: "remind"
                    , name: "学习提醒"
                    , avatar: "/static/images/im/remind.png"
                    , type: 1
                });
                im.funs.appendUser({
                    id: "qa"
                    , code: "qa"
                    , name: "智能答疑"
                    , avatar: "/static/images/im/qa.png"
                    , type: 2
                });
                im.funs.appendUser({
                    id: "recom"
                    , code: "recom"
                    , name: "学习推荐"
                    , avatar: "/static/images/im/recom.png"
                    , type: 3
                });
            }
            var host = window.location.host;
            var user_id = $("#im_user_info #myId").val();
            if ('WebSocket' in window) {
                im.vars.websocket = new ReconnectingWebSocket("ws://"
                    + host + "/imSocket?user_id=" + user_id, null, { debug: true, maxReconnectAttempts: 4 });
            } else if ('MozWebSocket' in window) {
                im.vars.websocket = new MozWebSocket("ws://" + host
                    + "/imSocket?user_id=" + user_id);
            } else {
                im.vars.websocket = new SockJS("http://" + host
                    + "/sockjs/imSocket?user_id=" + user_id);
            }
            im.vars.websocket.onopen = function (evnt) {
                console.log("open");
            };
            im.vars.websocket.onmessage = function (evnt) {
                console.log(evnt.data)
                var messages = JSON.parse(evnt.data);
                im.funs.receiveMsg(messages);
            };
            im.vars.websocket.onerror = function (evnt) {
                console.log("error");
            };
            im.vars.websocket.onclose = function (evnt) {
                console.log("close");
            }
            window.setInterval(function () {
                im.vars.websocket.send(JSON.stringify({
                    body: ""
                    , type: 5
                    , sender: im.vars.loginUser
                }));
            }, 200000);
            $("#chat-fasong").click(function () {
                var textContent = $(".div-textarea").html();
                if (textContent != "") {
                    var now = new Date();
                    var type = 0;
                    var userid = im.vars.getCurUser().id;
                    if (userid == "remind" || userid == "qa" || userid == "recom") {
                        type = 6
                    }
                    var msg = {
                        time: new Date().getTime()
                        , timeFmt: now.getFullYear() + "-" + (Array(2).join('0') + (now.getMonth() + 1)).slice(-2) + "-" + now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds()
                        , body: textContent
                        , type: type//0文本消息 1表情 2图片
                        , action: 1//0接收 1发送
                        , read: true//是否已读
                        , sender: im.vars.getLoginUser()
                        , receiver: im.vars.getCurUser()
                    };
                    im.funs.appendMsg(msg);
                    im.funs.sendMsg(msg);
                    //发送后清空输入框
                    $(".div-textarea").html("");
                    //聊天框默认最底部
                    $(document).ready(function () {
                        $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                    });
                }
            });

            $("#im_user_list_panel").on("click", ".chat-list-people", function () {
                var n = $(this).index();
                $(".chatBox-head-one").toggle();
                $(".chatBox-head-two").toggle();
                $(".chatBox-list").fadeToggle();
                $(".chatBox-kuang").fadeToggle();
                //传名字
                $(".ChatInfoName").text($(this).children(".chat-name").children("p").eq(0).html());
                //传头像
                $(".ChatInfoHead>img").attr("src", $(this).children().eq(0).children("img").attr("src"));
                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
                $("#chatBox-content-demo").html("");
                im.vars.curUser = im.vars.users[$(this).attr("data")];
                im.funs.readMsg(im.vars.curUser);
                $.each(im.vars.curUser.messages, function () {
                    im.funs.appendMsg(this);
                });
                $("#im_panel_user_name").html(im.vars.curUser.name);
            });

            $(".im_receiver_btn").on("click", ".sendMsgToReceiver", function () {
                var user = {
                    id: $(this).attr("userId")
                    , code: $(this).attr("userCode")
                    , avatar: $(this).attr("userAvatar")
                    , name: $(this).attr("userName")
                    , type: parseInt($(this).attr("userType"))
                };
                im.funs.appendUser(user);

                $(".chatBox").show();
                $("#id_" + user.id).trigger("click");
            });
            $(".emoji-picker-image").click(function () {
                var now = new Date();
                var msg = {
                    time: new Date().getTime()
                    , timeFmt: now.getFullYear() + "-" + (Array(2).join('0') + (now.getMonth() + 1)).slice(-2) + "-" + now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds()
                    , body: $(this).parent().html()
                    , type: 1//0文本消息 1表情 2图片
                    , action: 1//0接收 1发送
                    , read: true//是否已读
                    , sender: im.vars.getLoginUser()
                    , receiver: im.vars.getCurUser()
                };
                im.funs.appendMsg(msg);
                im.funs.sendMsg(msg);
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            });
        }
    }
}



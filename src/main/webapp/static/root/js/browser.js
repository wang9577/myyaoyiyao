$(function(){
    if(!Supports.arrow){
        $.get("/alert.shtml",function(h){
            $("body").prepend(h);
        });
    }
});
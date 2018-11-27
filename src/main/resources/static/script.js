var script = document.createElement('script');
script.type = 'text/javascript';
script.src = 'http://code.jquery.com/jquery-1.8.3.js';
document.head.appendChild(script);

window.onload = function () {
    document.getElementById('companyId').onchange();
     // document.getElementById('bossId').onchange();
};

function getUsersByCompany(companyId, userId){
    $.ajax({
        type: "POST",
        url: "/get-users-by-company",
        data: "companyId=" + companyId,
        success: function(msg){
            var mas = eval(msg);
            var el = document.getElementById('bossId');
            while(el.childNodes.length>0){
                el.removeChild(el.childNodes[el.childNodes.length-1]);
            }
            var optNull = document.createElement("option");
            optNull.text="";
            optNull.value = "";
            el.appendChild(optNull);

            for(var i=0;i<mas.length;i++){

                var opt = document.createElement("option");
                opt.text=mas[i].name;
                opt.value = mas[i].id;
                el.appendChild(opt);

                if(userId == mas[i].id){
                    opt.selected = true;

                }

            }



        }
    });
}

function deleteUser(id) {
    $.ajax({
        type: "POST",
        url: "/delete-user",
        data: "id=" + id,
        error: function (xhr, ajaxOptions, thrownError) {
            alert($.parseJSON(xhr.responseText).message);
        },
        success: function (data) {
           window.location = "/";
        }
    });

}

function deleteCompany(id) {
    $.ajax({
        type: "POST",
        url: "/delete-company",
        data: "id=" + id,
        error: function (xhr, ajaxOptions, thrownError) {
            alert($.parseJSON(xhr.responseText).message);
        },
        success: function (data) {
            window.location = "/company";
        }
    });

}







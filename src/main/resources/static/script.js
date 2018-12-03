

window.onload = function () {
   var company = $('#companyId');
   if(company.length>0){
       company[0].onchange();
   }

};

function getUsersByCompany(companyId, userId){
    $.ajax({
        type: "POST",
        url: "/get-users-by-company",
        data: "companyId=" + companyId +"&id="+ $('#userUpdateId').val(),
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

$(document).ready(function() {

    changePageAndSize();
});
function changePageAndSize() {
    $('#pageSizeSelect').change(function(evt) {
        var url_string = window.location.href;
        var url = new URL(url_string);
        var nameCompany = url.searchParams.get("nameCompany");
        var name = url.searchParams.get("name");
        var nameByCompany = url.searchParams.get("nameByCompany");
        if(name==null && nameCompany==null && nameByCompany==null){
            window.location.href = window.location.href.split('?')[0] + "?pageSize=" + this.value + "&page=1";
        }
        else if(nameCompany!=null) {
            window.location.href = window.location.href.split('?')[0] + "?pageSize=" + this.value + "&page=1" + "&nameCompany=" + nameCompany;
        }
        else if(name!=null && nameByCompany!= null){
            window.location.href = window.location.href.split('?')[0] + "?pageSize=" + this.value + "&page=1" + "&name=" + name + "&nameByCompany=" + nameByCompany;
        }
        else if(name!=null){
            window.location.href = window.location.href.split('?')[0] + "?pageSize=" + this.value + "&page=1" + "&name=" + name;
        }
        else if(nameByCompany!=null){
            window.location.href = window.location.href.split('?')[0] + "?pageSize=" + this.value + "&page=1" + "&nameByCompany=" + nameByCompany;
        }



    });
}

var toggler = document.getElementsByClassName("myCaret");
var i;

for (i = 0; i < toggler.length; i++) {
    toggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".myNested").classList.toggle("active");
        this.classList.toggle("myCaret-down");
    });
}






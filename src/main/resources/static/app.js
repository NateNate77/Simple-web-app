var app = angular.module('app', []);

app.config(['$locationProvider', function ($locationProvider){
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);

app.controller('UserUpdateCtrl', ['$scope','$location', 'UserUpdateService',
    function ($scope, $location, UserUpdateService) {

        UserUpdateService.getUserForUpdate($location.search().id).then(function(data){
            $scope.userName = data.data.user.name;
            $scope.companiesName = data.data.companyList;
            $scope.userId = data.data.user.id;
            var companyId;
            for(var i = 0; i<$scope.companiesName.length; i++){
                if($scope.companiesName[i].id==data.data.user.companyId){
                    companyId = i;
                }
            }

            $scope.selectedOption = $scope.companiesName[companyId];

            $scope.bossList = data.data.usersByCompany;
            var bossId;
            for(var j = 0; j<$scope.bossList.length; j++){

                if($scope.bossList[j].id==data.data.user.bossId){
                    bossId = j;
                }
            }
            $scope.selectedOptionBoss = $scope.bossList[bossId];

        });




        $scope.getUsersByCompany = function(companyId, userId) {
            UserUpdateService.getUsersByCompany(companyId, userId).then(function (data) {

                $scope.bossList = data.data;

            })
        }

        $scope.updateUser = function () {
            var id = document.getElementById("userUpdateId").value;
            var name = document.getElementById("nameUser").value;
            var companyId = document.getElementById("companyId").value;
            var bossId = document.getElementById("bossId").value;
            var parameter = JSON.stringify({name:name, companyId:companyId, bossId:bossId, id:id});

            UserUpdateService.updateUser(parameter).then(function (data) {

                if(data.data.status){
                   window.location.href = "/";

                }
                else {
                    document.getElementById("error").textContent=data.data.error;
                    document.getElementById("error").style.display = 'block';
                }

            })
        }

        $scope.deleteUser = function(){

            UserUpdateService.deleteUser($location.search().id).then(function(data){

                if(data.data.status){
                    window.location.href = "/";

                }
                else {
                    document.getElementById("error").textContent=data.data.error;
                    document.getElementById("error").style.display = 'block';
                }


            })
        }

    }]);

app.service('UserUpdateService',['$http', function ($http) {

    function getUserForUpdate(id) {
        return $http({
            method: 'GET',
            url: 'getUser-for-update?id=' + id
        });
    }
    
    function getUsersByCompany(companyId, id) {
        return $http({
            method: 'POST',
            url: 'get-users-by-company?companyId=' + companyId + "&id=" + id
        });
    }

    function updateUser(parameter) {
        return $http({
            method: 'POST',
            url: 'update-user',
            data: parameter
        });

    }

    function deleteUser(id) {
        return $http({
            method: 'POST',
            url: 'delete-user?id=' + id
        });
    }

    return {
        getUserForUpdate : getUserForUpdate,
        getUsersByCompany : getUsersByCompany,
        updateUser : updateUser,
        deleteUser: deleteUser
    };
}]);

app.controller('AddNewUserCtrl', ['$scope','AddNewUserService',
    function ($scope, AddNewUserService) {

    AddNewUserService.getCompanies().then(function (data) {

        $scope.companyList = data.data;
        $scope.selectedOption = $scope.companyList[0];
    })

        $scope.getUsersByCompany = function(companyId) {
            AddNewUserService.getUsersByCompany(companyId).then(function (data) {

                $scope.bossList = data.data;

            })
        }


        $scope.addNewUser = function () {

            var name = document.getElementById("nameUser").value;
            var companyId = document.getElementById("companyId").value;
            var bossId = document.getElementById("bossId").value;
            var parameter = JSON.stringify({name:name, companyId:companyId, bossId:bossId});

            AddNewUserService.addNewUser(parameter).then(function (data) {

                if(data.data.status){
                    window.location.href = "/";

                }
                else {
                    document.getElementById("error").textContent=data.data.error;
                    document.getElementById("error").style.display = 'block';
                }

            })
        }


    }]);

app.service('AddNewUserService',['$http', function ($http) {

    function getUsersByCompany(companyId) {
        return $http({
            method: 'POST',
            url: 'get-users-by-company-for-add-new-user?companyId=' + companyId
        });
    }

    function getCompanies() {
        return $http({
            method: 'GET',
            url: 'companies-for-add-new-user'
        });
    }

    function addNewUser(parameter) {
        return $http({
            method: 'POST',
            url: 'add-new-user',
            data: parameter
        });

    }

    return {

        getUsersByCompany : getUsersByCompany,
        getCompanies : getCompanies,
        addNewUser : addNewUser
    };
}]);


app.controller('CompanyUpdateCtrl', ['$scope','$location', 'CompanyUpdateService',
    function ($scope, $location, CompanyUpdateService) {

        CompanyUpdateService.getCompanyForUpdate($location.search().id).then(function(data){
            $scope.companyName = data.data.company.name;
            $scope.companiesName = data.data.companyList;
            $scope.companyId = data.data.company.id;
            var companyId;
            for(var i = 0; i<$scope.companiesName.length; i++){
                if($scope.companiesName[i].id==data.data.company.headCompanyId){
                    companyId = i;
                }
            }

            $scope.selectedOption = $scope.companiesName[companyId];

        });

        $scope.updateCompany = function () {
            var id = document.getElementById("companyUpdateId").value;
            var name = document.getElementById("nameCompany").value;
            var headCompanyId = document.getElementById("headCompanyId").value;
            var parameter = JSON.stringify({name:name, headCompanyId:headCompanyId, id:id});

            CompanyUpdateService.updateCompany(parameter).then(function (data) {

                if(data.data.status){
                    window.location.href = "/company";

                }
                else {
                    document.getElementById("error").textContent=data.data.error;
                    document.getElementById("error").style.display = 'block';
                }

            })
        }

        $scope.deleteCompany = function(){

            CompanyUpdateService.deleteCompany($location.search().id).then(function(data){

                if(data.data.status){
                    window.location.href = "/company";

                }
                else {
                    document.getElementById("error").textContent=data.data.error;
                    document.getElementById("error").style.display = 'block';
                }


            })
        }

    }]);

app.service('CompanyUpdateService',['$http', function ($http) {

    function getCompanyForUpdate(id) {
        return $http({
            method: 'GET',
            url: 'get-company-for-update?id=' + id
        });
    }

    function updateCompany(parameter) {
        return $http({
            method: 'POST',
            url: 'update-company',
            data: parameter
        });

    }

    function deleteCompany(id) {
        return $http({
            method: 'POST',
            url: 'delete-company?id=' + id
        });
    }

    return {
        getCompanyForUpdate : getCompanyForUpdate,
        updateCompany : updateCompany,
        deleteCompany: deleteCompany
    };
}]);


app.controller('AddNewCompanyCtrl', ['$scope','AddNewCompanyService',
    function ($scope, AddNewCompanyService) {

        AddNewCompanyService.getCompanies().then(function (data) {

            $scope.companyList = data.data;
            $scope.selectedOption = $scope.companyList[0];
        })


        $scope.addNewCompany = function () {

            var name = document.getElementById("nameCompany").value;
            var headCompanyId = document.getElementById("headCompanyId").value;
            var parameter = JSON.stringify({name:name, headCompanyId:headCompanyId});

            AddNewCompanyService.addNewCompany(parameter).then(function (data) {

                if(data.data.status){
                    window.location.href = "/company";

                }
                else {
                    document.getElementById("error").textContent=data.data.error;
                    document.getElementById("error").style.display = 'block';
                }

            })
        }


    }]);

app.service('AddNewCompanyService',['$http', function ($http) {


    function getCompanies() {
        return $http({
            method: 'GET',
            url: 'list-for-add-new-company'
        });
    }

    function addNewCompany(parameter) {
        return $http({
            method: 'POST',
            url: 'add-new-company',
            data: parameter
        });

    }

    return {

        getCompanies : getCompanies,
        addNewCompany : addNewCompany
    };
}]);
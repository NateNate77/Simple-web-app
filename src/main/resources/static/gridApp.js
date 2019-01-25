
var app = angular.module('gridApp', ['ui.grid','ui.grid.pagination']);

app.controller('UsersCtrl', ['$scope','UserService',
    function ($scope, UserService) {

        var paginationOptions = {
            pageNumber: 1,
            pageSize: 5,
            sort: null
        };



        $scope.gridOptions = {
            enableFiltering: true,
            paginationPageSizes: [5, 10],
            paginationPageSize: paginationOptions.pageSize,
            enableColumnMenus:false,
            columnDefs: [
                { name: 'id', enableFiltering: false },
                { name: 'name', cellTemplate: '<div><a ng-href="/update-user?id={{row.entity.id}}" title="Изменить">{{row.entity.name}}</a></div>' },
                { name: 'companyName' },
                { name: 'bossName', enableFiltering: false }
            ],
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
            }
        };

        UserService.getUsers().then(function(data){
            $scope.gridOptions.data = data.data;
            $scope.gridOptions.totalItems = data.data.length;
        });

    }]);

app.service('UserService',['$http', function ($http) {

    function getUsers() {
        return $http({
            method: 'GET',
            url: 'getUsers'
        });
    }
    return {
        getUsers: getUsers
    };
}]);

app.controller('CompaniesCtrl', ['$scope','CompaniesService',
    function ($scope, CompaniesService) {

        var paginationOptions = {
            pageNumber: 1,
            pageSize: 5,
            sort: null
        };



        $scope.gridOptions = {
            enableFiltering: true,
            paginationPageSizes: [5, 10],
            paginationPageSize: paginationOptions.pageSize,
            enableColumnMenus:false,
            columnDefs: [
                { name: 'id', enableFiltering: false, type: 'number' },
                { name: 'name', cellTemplate: '<div><a ng-href="/update-company?id={{row.entity.id}}" title="Изменить">{{row.entity.name}}</a></div>' },
                { name: 'usersCount', enableFiltering: false},
                { name: 'headCompanyName', enableFiltering: false }
            ],
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
            }
        };

        CompaniesService.getCompanies().then(function(data){
            $scope.gridOptions.data = data.data;
            $scope.gridOptions.totalItems = data.data.length;
        });

    }]);

app.service('CompaniesService',['$http', function ($http) {

    function getCompanies() {
        return $http({
            method: 'GET',
            url: 'getCompanies'
        });
    }
    return {
        getCompanies: getCompanies
    };
}]);

var app = angular.module('TreeApp', []);

app.controller('TreeController', ['$scope','TreeViewService',

    function($scope, TreeViewService) {


        TreeViewService.getCompaniesTreeView().then(function(data){

            $scope.list = data.data;

        });

    }
]);

app.service ('TreeViewService',['$http', function ($http) {
    function getCompaniesTreeView() {
        return $http({
            method: 'GET',
            url: 'get-companies-tree-view'
        });
    }


    return {
        getCompaniesTreeView: getCompaniesTreeView,

    };

}]);

app.controller('UserTreeController', ['$scope','UserTreeViewService',


    function ($scope, UserTreeViewService) {

        UserTreeViewService.getUserTreeView().then(function(data){

            $scope.list = data.data;

        });

    }

]);

app.service ('UserTreeViewService',['$http', function ($http) {


    function getUserTreeView() {
        return $http({
            method: 'GET',
            url: 'get-staff-tree-view'
        });
    }

    return {
        getUserTreeView : getUserTreeView
    };

}]);
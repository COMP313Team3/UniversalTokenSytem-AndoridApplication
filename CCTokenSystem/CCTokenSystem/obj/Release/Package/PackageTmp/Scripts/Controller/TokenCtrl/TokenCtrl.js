var app = angular.module('myWebAPI', ['ui.bootstrap']);

app.filter('startFrom', function () {
    return function (input, start) {
        if (input) {
            start = +start;
            return input.slice(start);
        }
        return [];
    };
});

app.controller("TokenCtrl", ['$scope','$http','filterFilter', function ($scope, $http,filterFilter) {

    $scope.renderTokenModels = function (response) {
        $scope.tokenView = false;
        $scope.tokenInfo = true;
        $scope.TokenData = response;

        $scope.currentPage = 1;
        $scope.totalItems = $scope.TokenData.length;
        $scope.entryLimit = 5; // items per page
        $scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);

        // $watch search to update pagination
        $scope.$watch('search', function (newVal, oldVal) {
            $scope.filtered = filterFilter($scope.TokenData, newVal);
            $scope.totalItems = $scope.filtered.length;
            $scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
            $scope.currentPage = 1;
        }, true);
    };

    $scope.TokenInfo = function () {
        $http.get("/api/tokens/RetrieveTokensByDept?dID=" + 1)
            .success($scope.renderTokenModels);
    }

    $scope.TokenInfo();

    $scope.Select = function (tokenid) {
        $http.get("/api/tokens/RetrieveTokenById?token_Id=" + tokenid)
            .success(function (response) {
                $scope.tokenView = true;
                $scope.tokenInfo = false;
                $scope.token = response;
            });
    };

    $scope.Close = function (token) {
        $http.put("/api/tokens/", token)
            .success(function (response) {
                $scope.tokenView = false;
                $scope.tokenView = true;
                $scope.TokenInfo();
            });
    };
    $scope.Cancel = function () {
        $scope.tokenView = false;
        $scope.tokenInfo = true;
    };
   
}]);


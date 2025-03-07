﻿var app = angular.module('myWebAPI', ['ui.bootstrap']);

app.filter('startFrom', function () {
    return function (input, start) {
        if (input) {
            start = +start;
            return input.slice(start);
        }
        return [];
    };
});

app.controller("TokenCtrl", ['$scope', '$http', 'filterFilter', function ($scope, $http, filterFilter) {
    $scope.tokenView = false;
    $scope.tokenInfo = false;
    $scope.out = false;
    $scope.log = true;
    $scope.login = function (advisor) {
        $http.get('/api/Advisor/GetAdvisor?email=' + advisor.email + '&password=' + advisor.password)
        .then(function (res) {
            if (res != "Not Found") {
                $scope.advisor = res.data[0];
                var ad_Id = res.data[0].Advisor_Id;
                //$scope.Advisor_Id = ad_Id;
                var dID = res.data[0].dept_Id;
                $scope.renderTokenModels = function (response) {
                    $scope.tokenView = false;
                    $scope.tokenInfo = true;
                    $scope.out = true;
                    $scope.log = false;
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
                    $http.get("/api/tokens/RetrieveTokensByDept?dID=" + dID)
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
                    if (token.Advisor_Id == 0) {
                        token.Advisor_Id = ad_Id;
                        $http.put("/api/tokens/", token)
                       .success(function (response) {
                           $scope.tokenView = false;
                           $scope.tokenView = true;
                           $scope.TokenInfo();
                       });
                    }
                    
                };
                $scope.Cancel = function () {
                    $scope.tokenView = false;
                    $scope.tokenInfo = true;
                };
                $scope.Signout = function () {
                    $scope.tokenView = false;
                    $scope.tokenInfo = false;
                    $scope.out = false;
                    $scope.log = true;
                }
            } else {
                $scope.message = "Invalid login credentials";
            }
        });
    };
}]);


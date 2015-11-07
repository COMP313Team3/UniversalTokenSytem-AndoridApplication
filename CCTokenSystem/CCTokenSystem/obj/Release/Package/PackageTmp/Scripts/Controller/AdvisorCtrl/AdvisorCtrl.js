var app = angular.module("myWebAPI", [])
app.controller("AdvisorCtrl", function ($scope, $http) {
    //Get all Department details
    $scope.renderDeptModels = function (response) {
        $scope.DeptData = response;
    };

    $scope.DeptInfo = function () {
        $http.get("/api/Department")
            .success($scope.renderDeptModels);
    }

    $scope.DeptInfo();
})
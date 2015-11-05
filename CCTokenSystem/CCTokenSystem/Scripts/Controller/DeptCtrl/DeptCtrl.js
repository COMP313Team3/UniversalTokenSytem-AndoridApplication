var app = angular.module("myWebAPI", [])

app.controller("DeptCtrl", function ($scope, $http) {

    $scope.renderCampusModel = function (response) {
        $scope.CampusData = response;
    };

    $scope.CampusInfo = function () {
        $http.get("/api/Campuses/")
            .success($scope.renderCampusModel);
    }
    $scope.CampusInfo();

    //Get all Department details
    $scope.renderDeptModels = function (response) {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.DeptData = response;
    };

    $scope.DeptInfo = function () {
        $http.get("/api/Department")
            .success($scope.renderDeptModels);
    }

    $scope.DeptInfo();


    //Add Department record into database
    $scope.Create = function (dept) {
        console.log(dept);
        $http.post("/api/Department", dept)
            .success(function (response) {
                $scope.DeptInfo();
            })
    };

    $scope.Remove = function (DeptId) {
        $http.delete("/api/Department/" + DeptId)
            .success(function (response) {
                $scope.DeptInfo();
            });
    };

    $scope.Select = function (DeptId) {
        $http.get("/api/Department/" + DeptId)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.dept = response;
            });
    };

    $scope.Update = function (dept) {
        console.log(dept);
        $http.put("/api/Department/" + dept.DeptId, dept)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.DeptInfo();
            });
    };

    $scope.Clear = function () {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.clearSt = false;
        $scope.dept = null;
    };

});
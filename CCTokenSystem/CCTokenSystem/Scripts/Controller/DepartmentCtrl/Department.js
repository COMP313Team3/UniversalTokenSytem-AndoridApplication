var app = angular.module("myWebAPI", [])
app.controller("DepartmentCtrl", function ($scope, $http) {

    //Get all stundet details
    $scope.renderDepartmentModel = function (response) {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.DepartmentData = response;
    };

    $scope.DepartmentInfo = function () {
        $http.get("/api/Departement/")
            .success($scope.renderDepartmentModel);
    }

    $scope.DepartmentInfo();


    //Add student record into database
    $scope.Create = function (department) {
        $http.post("/api/Departement", department)
            .success(function (response) {
                $scope.DepartmentInfo();
            })
    };

    $scope.Remove = function (DepartmentId) {
        $http.delete("/api/Departement/" + DepartmentId)
            .success(function (response) {
                $scope.DepartmentInfo();
            });
    };

    $scope.Select = function (DepartmentId) {
        $http.get("/api/Departement/" + DepartmentId)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.department = response;
            });
    };

    $scope.Update = function (department) {
        $http.put("/api/Departement/" + department.DepartmentId, department)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.DepartmentInfo();
            });
    };

    $scope.Clear = function () {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.clearSt = false;
        $scope.department = null;
    };
})
var app = angular.module("myWebAPI", [])
app.controller("CampusCtrl", function ($scope, $http) {

    //Get all stundet details
    $scope.renderCampusModel = function (response) {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.CampusData = response;
    };

    $scope.CampusInfo = function () {
        $http.get("/api/Campuses/")
            .success($scope.renderCampusModel);
    }

    $scope.CampusInfo();


    //Add student record into database
    $scope.Create = function (campus) {
        $http.post("/api/Campuses", campus)
            .success(function (response) {
                $scope.CampusInfo();
            })
    };

    $scope.Remove = function (CampusId) {
        $http.delete("/api/Campuses/" + CampusId)
            .success(function (response) {
                $scope.CampusInfo();
            });
    };

    $scope.Select = function (CampusId) {
        $http.get("/api/Campuses/" + CampusId)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.campus = response;
            });
    };

    $scope.Update = function (campus) {
        $http.put("/api/Campuses/" + campus.CampusId, campus)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.CampusInfo();
            });
    };

    $scope.Clear = function () {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.clearSt = false;
        $scope.campus = null;
    };
})
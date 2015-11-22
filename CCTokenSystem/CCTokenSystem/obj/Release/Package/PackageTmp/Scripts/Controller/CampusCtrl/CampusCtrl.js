var app = angular.module("myWebAPI", [])
app.controller("CampusCtrl", function ($scope, $http) {

    //Get all stundet details
    $scope.renderCampusModel = function (response) {
        $scope.c_Name = "";
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
        console.log(campus);
        $http.post("/api/Campuses", campus)
            .success(function (response) {
                if (response != "Not Found") {
                    $scope.CampusInfo();
                    $scope.c_Name = "";
                } else {
                    $scope.c_Name = "Campus Name already exists";
                }
            })

    };

    $scope.Remove = function (CampusId) {
        $http.delete("/api/Campuses/" + CampusId)
            .success(function (response) {
                $scope.c_Name = "";
                $scope.CampusInfo();
            });
    };

    $scope.Select = function (CampusId) {
        $http.get("/api/Campuses/" + CampusId)
            .success(function (response) {
                $scope.c_Name = "";
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.campus = response;
            });
    };

    $scope.Update = function (campus) {
        $http.put("/api/Campuses/" + campus.CampusId, campus)
            .success(function (response) {
                $scope.c_Name = "";
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.CampusInfo();
            });
    };

    $scope.Clear = function () {
        $scope.c_Name = "";
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.clearSt = false;
        $scope.campus = null;
    };
})
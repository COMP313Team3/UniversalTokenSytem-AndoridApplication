var app = angular.module("myWebAPI", [])
app.controller("AdvisorCtrl", function ($scope, $http) {

    $scope.renderCampusModel = function (response) {
        $scope.CampusData = response;
    };

    $scope.CampusInfo = function () {
        $http.get("/api/Campuses")
            .success($scope.renderCampusModel);
    }
    $scope.CampusInfo();
    $scope.select = function (campus_Id) {
        console.log(campus_Id);
        $http.get("/api/Department?CampusId=" + campus_Id)
           .success(function (response) {
               console.log(response);
                $scope.DeptData = response;
           });
            
    }
   
    $scope.renderAdvisorModels = function (response) {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.AdvisorData = response;
    };

    $scope.AdviosrInfo = function () {
        $http.get("/api/Advisor")
            .success($scope.renderAdvisorModels);
    }

    $scope.AdviosrInfo();


    //Add Department record into database
    $scope.Create = function (advisor) {
        $http.post("/api/Advisor/", advisor)
            .success(function (response) {
                $scope.AdviosrInfo();
            })
    };

    $scope.Remove = function (Advisor_Id) {
        $http.delete("/api/Advisor/" + Advisor_Id)
            .success(function (response) {
                $scope.DeptInfo();
            });
    };

    $scope.Select = function (Advisor_Id) {
        $http.get("/api/Advisor/" + Advisor_Id)
            .success(function (response) {
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.dept = response;
            });
    };

    $scope.Update = function (advisor) {
        $http.put("/api/Advisor/" + advisor.Advisor_Id, advisor)
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
})
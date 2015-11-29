var app = angular.module("myWebAPI", [])
app.controller("AdvisorCtrl", function ($scope, $http) {



    $scope.renderCampusModel = function (response) {

        $scope.advisor = {};
        $scope.CampusData = response;
        $scope.advisor.campusid = $scope.CampusData[0].CampusId;

        $http.get("/api/Department?CampusId=" + $scope.CampusData[0].CampusId)
          .success(function (response) {    
              $scope.DeptData = response;
              $scope.advisor.dept_id = response[0].dept_Id;
          });
    };

    $scope.CampusInfo = function () {
        $http.get("/api/Campuses")
            .success($scope.renderCampusModel);
    }
    $scope.CampusInfo();
    $scope.select = function (campus_Id) {
        $http.get("/api/Department?CampusId=" + campus_Id)
           .success(function (response) {
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
                if (response != "Not Found") {
                    $scope.AdviosrInfo();
                    $scope.a_Name = "";
                } else {
                    $scope.a_Name = "Email already exists";
                }
            })
    };

    $scope.Remove = function (Advisor_Id) {
        $http.delete("/api/Advisor/" + Advisor_Id)
            .success(function (response) {
                $scope.a_Name = "";
                $scope.DeptInfo();
            });
    };

    $scope.Select = function (Advisor_Id) {
        $http.get("/api/Advisor/" + Advisor_Id)
            .success(function (response) {
                $scope.a_Name = "";
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.advisor = response;

                $http.get("/api/Department?CampusId=" + $scope.advisor.campusid)
                 .success(function (response) {
                     $scope.DeptData = response;
              });

                $scope.advisor.dept_id = response.dept_Id;
            });
    };

    $scope.Update = function (advisor) {
        $http.put("/api/Advisor/" + advisor.Advisor_Id, advisor)
            .success(function (response) {
                $scope.a_Name = "";
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.DeptInfo();
            });

        $scope.AdviosrInfo();
    };

    $scope.Clear = function () {
        $scope.a_Name = "";
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.clearSt = false;
        $scope.dept = null;
    };
})
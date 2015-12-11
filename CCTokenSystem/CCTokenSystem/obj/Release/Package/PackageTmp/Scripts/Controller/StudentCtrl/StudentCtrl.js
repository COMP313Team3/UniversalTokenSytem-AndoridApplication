var app = angular.module("myWebAPI", [])

app.controller("StudentCtrl", function ($scope, $http) {
    //Get all stundet details

    
    $scope.renderStudentModels = function (response) {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.StData = response;
        console.log(response);
    };

    $scope.StudentInfo = function () {
        $http.get("/api/Students")
            .success($scope.renderStudentModels);
    }

    $scope.StudentInfo();


    //Add student record into database
    $scope.Create = function (student) {
        $http.post("/api/Students", student)
            .success(function (response) {
                if (response != "Not Found") {
                    $scope.StudentInfo();
                    $scope.st_id = "";
                } else {
                    $scope.st_id = "Student ID already exists";
                }
            })
    };

    $scope.Remove = function (Id) {
        $http.delete("/api/Students/" + Id)
            .success(function (response) {
                $scope.st_id = "";
                $scope.StudentInfo();
            });
    };

    $scope.Select = function (Id) {
        $http.get("/api/Students/" + Id)
            .success(function (response) {
                $scope.st_id = "";
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.clearSt = true;
                $scope.student = response;
            });
    };

    $scope.Update = function (student) {
        $http.put("/api/Students/" + student.Id, student)
            .success(function (response) {
                $scope.st_id = "";
                $scope.addSt = false;
                $scope.updateSt = true;
                $scope.StudentInfo();
            });
    };

    $scope.Clear = function () {
        $scope.addSt = true;
        $scope.updateSt = false;
        $scope.clearSt = false;
        $scope.student = null;
    };
});


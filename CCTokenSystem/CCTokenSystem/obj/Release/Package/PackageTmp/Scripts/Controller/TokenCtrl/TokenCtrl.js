var app = angular.module("myWebAPI", [])

app.controller("TokenCtrl", function ($scope, $http) {
    $scope.renderTokenModels = function (response) {
        $scope.tokenView = false;
        $scope.TokenData = response;
    };

    $scope.TokenInfo = function () {
        $http.get("/api/tokens/RetrieveTokensByDept?dID="+1)
            .success($scope.renderTokenModels);
    }

    $scope.TokenInfo();

    $scope.Select = function (tokenid) {
        $http.get("/api/tokens/RetrieveTokenById?token_Id=" + tokenid)
            .success(function (response) {
                $scope.tokenView = true;
                $scope.token = response;
            });
    };

    $scope.Close = function (token) {
        console.log(token);
        $http.put("/api/tokens/", token)
            .success(function (response) {
                console.log(response);
                $scope.tokenView = false;
                $scope.TokenInfo();
            });
    };
});


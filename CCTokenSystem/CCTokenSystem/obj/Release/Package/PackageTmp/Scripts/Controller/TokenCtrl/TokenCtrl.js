var app = angular.module("myWebAPI", [])

app.controller("TokenCtrl", function ($scope, $http) {
    $scope.renderTokenModels = function (response) {
        $scope.tokenView = false;
        $scope.tokenInfo = true;
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
                $scope.tokenInfo = false;
                $scope.token = response;
            });
    };

    $scope.Close = function (token) {
        $http.put("/api/tokens/", token)
            .success(function (response) {
                $scope.tokenView = false;
                $scope.tokenView = true;
                $scope.TokenInfo();
            });
    };
    $scope.Cancel = function () {
        $scope.tokenView = false;
        $scope.tokenInfo = true;
    };
});


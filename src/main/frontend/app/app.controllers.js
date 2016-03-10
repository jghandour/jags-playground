"use strict";
app.controller("AppCtrl", ["ApiService", "$rootScope", "$http", function (ApiService, $rootScope, $http) {

    console.log("Initializing Application Controller...");

    $rootScope.csrf;
    $rootScope.version;

    $rootScope.getCsrf = function () {
        $rootScope.csrf = null;
        ApiService.doGet("/csrf", [], function (data) {
            $rootScope.csrf = data.result;
            console.log("Received CSRF: " + $rootScope.csrf);

            // Set CSRF Http Header
            $http.defaults.headers.common[$rootScope.csrf.headerName] = $rootScope.csrf.token;
        });
    }

    $rootScope.getVersion = function () {
        $rootScope.version = null;
        ApiService.doGet("/version", [], function (data) {
            $rootScope.version = data.result;
            console.log("Received Version: " + $rootScope.version);
        });
    }

    $rootScope.getCsrf();
    $rootScope.getVersion();

}]);
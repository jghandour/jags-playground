// API Service
app.factory("ApiService", ["$http", function ($http) {
    //$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";

    console.log("Initializing API Service...");

    function errorCallback(data, status, headers, config) {
        console.log("ERROR = " + data);
        alert(data);
    }

    function doHttp(method, url, params, successCallback) {
        return $http({method: method, params: params, url: url}).then(function(data){successCallback(data.data);}, errorCallback);
    }

    return {
        doGet: function (relativeUrl, params, successCallback) {
            return doHttp("GET", relativeUrl, params, successCallback);
        },
        doPut: function (relativeUrl, params, callback) {
            return doHttp("PUT", relativeUrl, params, successCallback);
        },
        doPost: function (relativeUrl, params, callback) {
            return doHttp("POST", relativeUrl, params, successCallback);
        }

    };
}]);
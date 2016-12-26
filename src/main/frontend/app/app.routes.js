console.log("Initializing Application Routes...");

app.config(["$routeProvider", "$locationProvider", function ($routeProvider, $locationProvider) {

    $locationProvider.hashPrefix('');

    $routeProvider
        .when("/", {templateUrl: "app/home.html"})
        .otherwise("/404", {templateUrl: "app/404.html"});
}]);

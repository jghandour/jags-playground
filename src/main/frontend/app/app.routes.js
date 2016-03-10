console.log("Initializing Application Routes...");

app.config(["$routeProvider", function ($routeProvider) {
    $routeProvider
        .when("/", {templateUrl: "app/home.html"})
        .otherwise("/404", {templateUrl: "app/404.html"});
}]);
